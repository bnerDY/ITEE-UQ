#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <fcntl.h>
#include "sim.h"
#include "util.h"
#include "errs.h"

agent_t* find_agent(sim_t* sim, pid_t pid) {
    for (uint i=0;i<sim->agcount;++i) {  
        if (sim->ags[i]->pid==pid) {
	    return sim->ags[i];
	}
    }
    return 0;	// should never happen
}

// Don't call unless children are set to auto-reap
void endchildren(sim_t* sim) {
    for (uint i=0;i<sim->agcount;++i) { 
        if ((sim->ags[i]->alive) && (sim->ags[i]->pid>0)) {
	    sim->ags[i]->alive=false;
	    kill(sim->ags[i]->pid, SIGKILL);  	    
	}
    }  
}

int init_sim(sim_t* sim, char* mapfile, char* agentfile) {
    char c1,c2;
    uint h,w;
    int result;
    FILE* mfile=fopen(mapfile,"r");
    if (mfile==0) {
        //print_msg(ERR_MAPFILE,'?',-1);
        return ERR_MAPFILE;
    }
    if ((fscanf(mfile, "%u%c%u%c", &h, &c1, &w, &c2)!=4) || (c2!='\n')){
        fclose(mfile);
        //print_msg(ERR_MAPREAD,'?',-1);
        return ERR_MAPREAD;    
    }
    map_t* map=alloc_map(h, w);
    result=fill_grid(map, mfile, '.');
    if (result) {
        fclose(mfile);
	free_map(map);
        //print_msg(result,'?',-1);
        return result;         
    }
    fclose(mfile);
    FILE* agfile=fopen(agentfile,"r");
    if (agfile==0) {
        free_map(map);
        //print_msg(ERR_AGFILE, '?',-1);
	return ERR_AGFILE;
    }
    uint t;  	    
    // Now to find out how many agents we have
    sim->ags=read_agentfile(agfile, &t);
    if (sim->ags==0) {
        free_map(map);
	fclose(agfile);
	    // we don't need to clean up the array because it is empty
        //print_msg(ERR_AGREAD, '?',-1);
	return ERR_AGREAD;	
    }
    sim->agcount=t;
    sim->map=map;
    return 0;
}

// whatever happens, we aren't coming back from this call
void start_child(agent_t* agent, int arr[4]) {
    // steps
    // 1) dup and close
    // 2) exec
    // 3) shutdown coz it didn't work
    close(arr[1]);
    close(arr[2]);
    close(arr[4]);	// read end of our error pipe
    fcntl(arr[5],F_SETFD, FD_CLOEXEC);	// to ensure the pipe closes
    if (dup2(arr[0], 0)!=-1) {
        close(arr[0]);
	if (dup2(arr[3], 1)!=-1) {
	    close(arr[3]);
	    execvp(agent->argv[0], agent->argv);
	}
    }
    // something failed
    write(arr[5], "F", 1);
    exit(99);
}

bool update_parent(agent_t* ag, int arr[6], pid_t cpid) {
    char buff;
    close(arr[0]);
    close(arr[3]);
    close(arr[5]);
    ag->to=fdopen(arr[1], "w");
    ag->from=fdopen(arr[2], "r");
    if (read(arr[4], &buff, 1)!=0) {
        // as far as we can tell, exec failed
        // reap the child and close all the fds
        waitpid(cpid, 0, 0);
        for (int i=0;i<6;++i) {
	    close(arr[i]);
	}
	return false;
    }
    close(arr[4]);
    close(arr[5]);
    // we have a working child
    ag->pid=cpid;
    return true;
}

bool start_agents(sim_t* sim) {
    bool haderr=false;
    int i;
    for (i=0;i<sim->agcount;++i) {
        int pipefds[6];
	if (pipe(pipefds)) {
	    haderr=true;
	    break;
	}
	if (pipe(pipefds+2)) {
	    haderr=true;
	    close(pipefds[0]);
	    close(pipefds[1]);
	    break;
	}
	if (pipe(pipefds+4)) {
	    for (int i=0;i<4;++i)
	        close(pipefds[i]);
	    haderr=true;
	    break;
	}
        pid_t pid=fork();
	if (pid<0) {
	    for (int i=0;i<6;++i)
	        close(pipefds[i]);
	    haderr=true;
	    break;
	}
	if (pid==0) {	// child
	    start_child(sim->ags[i], pipefds);	// we don't come back from this call
	}  
	if (!update_parent(sim->ags[i], pipefds, pid)) {
	    haderr=true;
	    break;
	}
    }
    if (haderr) {
        // now we will loop through the stuff that was inited successfully and clean up
        for (int j=0;j<i;++j) {		  
	    cleanup_agent(sim->ags[j]);
	    free(sim->ags[j]);
	}
        return false;
    }
    return true;
}

bool legal_reply(char c) {
    if ((c=='H') || (c=='D') || (c=='N') || (c=='S') || (c=='E') || (c=='W')) {
        return true;
    }
    return false;
}

int proc_move(sim_t* sim, uint agnum, char c) {
    int drow, dcol;
    if (c=='H') {
        show_map(sim->map);
        return 0;	// if we were ok before, we are ok now.
    }
    getDirVector(c, &drow, &dcol);
    int nrow=sim->ags[agnum]->r+drow;
    int ncol=sim->ags[agnum]->c+dcol;
    if ((nrow<0) || (ncol<0) || (nrow>=sim->map->h) || (ncol>=sim->map->w)) {
        print_msg(ERR_WALL, sim->ags[agnum]->s, 0);
	return ERR_WALL;	// moved outside the arena
    }
    if (sim->map->grid[nrow][ncol]=='#') {
        print_msg(ERR_WALL, sim->ags[agnum]->s, 0);
	return ERR_WALL;	// hit a wall      
    }
        // now we need to check if any other agent is at this position
    for (uint i=0;i<sim->agcount;++i) {
        if (i!=agnum) {
	    if ((sim->ags[i]->r==nrow) && (sim->ags[i]->c==ncol)) {
	        print_msg(ERR_CRASH,sim->ags[agnum]->s, 0);
		return ERR_CRASH;
	    }
	}
    }
        // so the move is valid, erase old mark
    sim->map->grid[sim->ags[agnum]->r][sim->ags[agnum]->c]='.';
    sim->map->grid[nrow][ncol]=sim->ags[agnum]->s;
    sim->ags[agnum]->r=nrow;
    sim->ags[agnum]->c=ncol;
    show_map(sim->map);
    return 0;
}

bool send_update(sim_t* sim, uint agnum) {
    for (uint i=0;i<sim->agcount;++i) {
        if (fprintf(sim->ags[agnum]->to, "%u %u\n", (sim->ags[i]->r)+1, (sim->ags[i]->c)+1)<0) {
	    return false;
	}
    }
    if (fflush(sim->ags[agnum]->to)==EOF) {
        return false;
    }    
    return true;
}


// returns false if we can't get chars
bool get_move(sim_t* sim, uint agnum, char* c) {
    int res=fgetc(sim->ags[agnum]->from);
    int res2=fgetc(sim->ags[agnum]->from);	// to clear \n
    if ((res==EOF) || (res2==EOF) || (res2!='\n')){
        while (res=fgetc(sim->ags[agnum]->from), (res!=EOF) && (res!='\n')){}
        return false;
    }
    *c=(char)res;  
    return true;
}

// I suspect would mainly be a problem if pipes are not configured correctly
bool send_init_data(sim_t* sim) {
    int err=0;
    for (uint i=0;i<sim->agcount;++i) {
        fprintf(sim->ags[i]->to, "%u\n", sim->agcount);
	for (uint j=0;j<sim->agcount;++j) {
            fputc(sim->ags[j]->s, sim->ags[i]->to);	    
	}
	fprintf(sim->ags[i]->to, "\n%u\n%u %u\n", i+1, sim->map->h, sim->map->w);
	for (uint r=0;r<sim->map->h;r++) {
	    for (uint c=0;c<sim->map->w;++c) {
	        if (sim->map->grid[r][c]=='#') {
		    fputc('#', sim->ags[i]->to);
		} else {
		    fputc(' ', sim->ags[i]->to);
		}
	    }
	    err|=(fputc('\n', sim->ags[i]->to)==EOF);
	}
	err|=fflush(sim->ags[i]->to);
    }
    if (err) {
        return false;
    }
    return true;
}

void stamp_agents(sim_t* sim) {
    for (uint i=0;i<sim->agcount;++i) { 
        sim->map->grid[sim->ags[i]->r][sim->ags[i]->c]=sim->ags[i]->s;
    }
}

int run_sim(sim_t* sim, uint maxs) {
    uint numsteps=0;
    stamp_agents(sim);	// put chars onto the map
    send_init_data(sim);	// naughty ignored return value
//    show_map(sim->map);
    do {   
//printf("Round %d\n", numsteps);
        for (uint i=0;i<sim->agcount;++i) {
	    if (!send_update(sim, i)) {
	        // a failure here means the agent exited
// 	        int stat;
// 		waitpid(sim->ags[i]->pid, &stat, 0);	// to be really paranoid I could have used NOHANG
// 		if (WIFEXITED(stat)) {
// 	            print_msg(ERR_CLOSE, sim->ags[i]->s, WEXITSTATUS(stat));
// 		    return ERR_CLOSE;
// 		} else {
// 		    print_msg(ERR_SIGCLOSE, sim->ags[i]->s, WTERMSIG(stat));
// 		    return ERR_SIGCLOSE;
// 		}
	    }
/*	    
	    if (!sim->ags[i]->alive) {
	        int stat=sim->ags[i]->status;
		if (WIFEXITED(stat)) {
		    if (WEXITSTATUS(stat)!=0) {
			print_msg(ERR_CLOSE, sim->ags[i]->s, WEXITSTATUS(stat));
			return ERR_CLOSE;
		    }
		} else {
		    print_msg(ERR_SIGCLOSE, sim->ags[i]->s, WTERMSIG(stat));
		    return ERR_SIGCLOSE;
		}	      
	    }
*/
	    char c='X';	// to record the move
	    if (!get_move(sim, i, &c)) {   
	    }


	    if (!sim->ags[i]->alive) {
	        int stat=sim->ags[i]->status;
		if (WIFEXITED(stat)) {
		    if (WEXITSTATUS(stat)!=0) {
			print_msg(ERR_CLOSE, sim->ags[i]->s, WEXITSTATUS(stat));
			return ERR_CLOSE;
		    }
		} else {
		    print_msg(ERR_SIGCLOSE, sim->ags[i]->s, WTERMSIG(stat));
		    return ERR_SIGCLOSE;
		}	      
	    }
	    if (!legal_reply(c)) {      
	        print_msg(ERR_REPLY, sim->ags[i]->s, 0);
		return ERR_REPLY;	      
	    }
	       // move is valid so keep going
	    //show_map(sim->map);
	    // So this will also trigger with an agent sending back a Dependencies 
	    // not met error
	    if (c=='D') {	      
	        print_msg(SUCCESS, sim->ags[i]->s, 0);
		return SUCCESS;
	    }
	    int result=proc_move(sim, i, c);
	    //show_map(sim->map);
	    if (result) {
                return result;		// the print has already happened in proc_move;
	    }
	}
	numsteps++;
    } while (numsteps<maxs);
    print_msg(ERR_STEPS, '?', 0);
    return ERR_STEPS;
}
