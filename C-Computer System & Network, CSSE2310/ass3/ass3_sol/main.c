#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#include "errs.h"
#include "sim.h"

sim_t* simulation=0;

void childcleanup(int s) {
    int res;
    int status;
    if (simulation==0) {
        return;
    }
    while (res=waitpid(0, &status, WNOHANG), res>0) {
        agent_t* ag=find_agent(simulation, res);
        if (ag!=0) {
	    ag->alive=false;
	    ag->status=status;
	}
    }
}

void inthandle(int s) {
    if (simulation!=0) {
        endchildren(simulation);
	simulation=0;
    }
    exit(14);
}

int main(int argc, char** argv) {
    uint maxsteps;
    long ms; 
    char* err;
    int result;
    struct sigaction sa;
    //memset(&sa, 0 , sizeof(struct sigaction)); 
    sa.sa_handler=SIG_IGN;
    sa.sa_flags=SA_RESTART;
    sigaction(SIGPIPE, &sa, 0);
    

   
    if (argc!=4) {
        print_msg(ERR_ARGS,'?', -1);
	return ERR_ARGS;
    }
    ms=strtol(argv[2], &err, 10);
    if ((ms<1) || (*err!='\0') || (ms>INT_MAX)) {
        print_msg(ERR_MAX, '?', -1);
        return ERR_MAX; 
    }
    maxsteps=(uint)ms;
    sim_t* sim=malloc(sizeof(sim_t));
    result=init_sim(sim, argv[1], argv[3]);
    if (result) {
        print_msg(result, '?', -1);
	return result; 
    }
    sa.sa_handler=inthandle;
    sigaction(SIGINT, &sa, 0);
    
    sa.sa_handler=childcleanup;
    sa.sa_flags|=SA_NOCLDSTOP;
    sigaction(SIGCHLD, &sa, 0);    
    
    
    
    simulation=sim;	// now that we know we have a valid one
    if (!start_agents(simulation)) {
        print_msg(ERR_START, '?', 0);
	return ERR_START;
    }
    result=run_sim(simulation, maxsteps);	// need to look at the return value of this
    // destroy the simluation
    simulation=0;
    endchildren(sim);
    return result;
}
