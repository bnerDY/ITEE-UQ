#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include "clutil.h"

#define CL_GOAL 0
#define CL_PCOUNT 1
#define CL_PVALS 2
#define CL_DEPS 3
#define CL_COM 4



#define DONE(X) print_cmsg(X);return X;

void print_cmsg(int e) {
    switch (e) {
      case CL_GOAL: printf("D\n");break;
      case CL_PCOUNT: printf("Incorrect number of params.\n"); break;
      case CL_PVALS: printf("Invalid params.\n"); break;
      case CL_DEPS: printf("Dependencies not met.\n"); break;
      case CL_COM: printf("Handler communication breakdown.\n"); break;
      default: break;
    }
    fflush(stdout);
}

void pipehandler(int s) {
    print_cmsg(CL_COM);
    exit(CL_COM);
}


void free_client(clientinfo_t* c) {
    free(c->chars);
    free_map(c->map);
    free(c->pos);
    free(c);
}

char getmove(uint r1, uint c1, uint r2, uint c2) {
    if ((r1==r2) && (c1==c2))  {
        return 'H';
    }
    if (r1<r2) {
        return 'S';
    }
    if (r1>r2) {
        return 'N';
    }
    if (c1>c2) {
        return 'W';
    }
    return 'E';
}

char fwdmove(int r, int c) {
    if (r==-1) {
        return 'N';
    }
    if (r==1) {
        return 'S';
    }
    // r==0
    if (c==-1) {
        return 'W';
    }
    return 'E';
}

// Need to read, the total number of agents, line of chars, number of this agent, map dims, map
clientinfo_t* setup() {
    char line[80];
    uint agcount;
    uint agnum;
    struct sigaction sa;
    sa.sa_handler=pipehandler;
    sa.sa_flags=SA_RESTART;
    sigaction(SIGPIPE, 0, &sa);     
    if (!fgets(line, 80, stdin) || (line[strlen(line)-1]!='\n')) {
        return 0;
    } 
    if (!sscanf(line, "%u", &agcount)) {
        return 0;
    }
        // we can't use a fixed size buffer here because we don't know how many agents
    char* cs=malloc(sizeof(char)*(agcount+1));
    for (int i=0;i<agcount;++i) {
        cs[i]=fgetc(stdin);
	if (feof(stdin) || (cs[i]=='\n')) {
	    free(cs);
	    return 0;
	}
    }
    (void)getchar();	// to clear end of line char
    cs[agcount]='\0';	// to make it easier for debug printing
    if (!fgets(line, 80, stdin) || (line[strlen(line)-1]!='\n')) {
        free(cs);
        return 0;
    }

    if (!sscanf(line, "%u", &agnum)) {
        free(cs);
        return 0;
    }
    agnum-=1;	// to make array lookups easier
    if ((agnum<0) || (agnum>=agcount)) {
        free(cs);
	return 0;
    }
    if (!fgets(line, 80, stdin) || (line[strlen(line)-1]!='\n')) {
        free(cs);
        return 0;
    }
    uint rows, cols;
    if (sscanf(line,"%u %u", &rows, &cols)!=2) {
        free(cs);
        return 0;
    }
    map_t* map=alloc_map(rows, cols);
    if (fill_grid(map, stdin, ' ')) {
        free_map(map);
	free(cs);
        return 0;
    }
    // now we have all the parts
    clientinfo_t* client=malloc(sizeof(clientinfo_t));
    client->agcount=agcount;
    client->agnum=agnum;
    client->chars=cs;
    client->map=map;
    client->pos=malloc(sizeof(uint)*2*agcount);
    return client;
}

bool getTurn(clientinfo_t* client) {
    char buff[80];
    uint r,c;
    for (int i=0;i<client->agcount;++i) {
	if (!fgets(buff, 80, stdin) || (sscanf(buff, "%u %u", &r, &c)!=2)) {
//fprintf(stderr, "Error: [%s]\n", buff);	
	    return false;
	}
	client->pos[2*i]=r-1;
	client->pos[2*i+1]=c-1;
    }
    return true;
}

// sets the direction to 90 right of current
void rotateright(int* r, int* c) {
    if (*r==-1) {*r=0; *c=1;}
    else if (*r==1) {*r=0; *c=-1;}   
    else if (*c==-1) {*r=-1; *c=0;}
    else {
	*r=1;
	*c=0;    
    }
}

char getDir(int r, int c) {
    if (r==1) return 'S';
    if (r==-1) return 'N';
    if (c==1) return 'E';
    return 'W';
}

bool isagent(clientinfo_t* cl, int row, int col) {
    for (uint i=0;i<cl->agcount;++i) {
	if ((cl->pos[2*i]==row) && (cl->pos[2*i+1]==col)) {
	    return true;
	}
    }
    return false;
}

char backleftcorner(clientinfo_t* client, int dr, int dc) {
    if (dr==1) {	// south
        dr=-1;
	dc=1;
      
    } else if (dr==-1) {	// north
        dr=1;
	dc=-1;
      
    } else {	// horizontal movement
        if (dc==1) {	//east
	  dr=-1;
	  dc=-1;
	} else {	// west
	  dr=1;
	  dc=1;
	}
    }
    int nr=client->pos[2*client->agnum]+dr;
    int nc=client->pos[2*client->agnum+1]+dc;  
    if (inbounds(client->map, nr,nc)) {
        return client->map->grid[nr][nc];
    }
    return '#';
}

char leftchar(clientinfo_t* client, int dr, int dc) {
    if (dr==-1) {	// north
        dr=0;
	dc=-1;
    } else if (dr==1) {	// south
        dr=0;
	dc=1;
      
    } else {	// horizontal movement
        if (dc==-1) {	// west
	  dr=1;
	  dc=0;
	} else {	// east
	  dr=-1;
	  dc=0;
	}
    }
    int nr=client->pos[2*client->agnum]+dr;
    int nc=client->pos[2*client->agnum+1]+dc;    
    if (inbounds(client->map, nr,nc)) {
        return client->map->grid[nr][nc];
    }
    return '#';
}

char fwdchar(clientinfo_t* client, int dr, int dc) {
    int nr=client->pos[2*client->agnum]+dr;
    int nc=client->pos[2*client->agnum+1]+dc;      
    if (inbounds(client->map, nr,nc)) {
        return client->map->grid[nr][nc];
    }
    return '#';  
}


/* generates a move according to the walling algorithm
Three cases (agent is at > moving east:
  #.
   >      Rotate left
   
  #
  >       Move ahead
  
  
  
  .       
  >       Move ahead
*/   
void wallmove(clientinfo_t* client, int* dr, int* dc) {
    char backleft=backleftcorner(client, *dr, *dc);
    char left=leftchar(client, *dr, *dc);
    if ((backleft=='#') && (left==' ')) {
      
// printf("Triggered left turn\n");
// fflush(stdout);
	rotateright(dr, dc);
	rotateright(dr, dc);	// lazy left turn
	rotateright(dr, dc);
	// now either this is clear or blocked by agent
	if (blockedbyagent(client, *dr, *dc)) {
	    printf("H\n");
	    fflush(stdout);
	    return;
	}
	// not blocked so move forward
	printf("%c\n", fwdmove(*dr, *dc));
	fflush(stdout);
	return;
    }
    for (int i=0;i<4;++i) {
        if ((fwdchar(client, *dr, *dc)==' ')) {
	    if (blockedbyagent(client, *dr, *dc)) {
	        printf("H\n");
		fflush(stdout);
		return;
	    }
	    	    // not blocked so move forward
	    printf("%c\n", fwdmove(*dr, *dc));
	    fflush(stdout);
	    return;
	}
	// we must be blocked fwd so rotate and try again
	rotateright(dr, dc);
    }	
    // if we get here, we must be surrounded
    printf("H\n");
    fflush(stdout);
}


// starts at our position, moves in the direction until it hits wall, oobounds or agent
bool sightonagent(clientinfo_t* cl, int dr, int dc, uint agid) {
    int row=cl->pos[2*cl->agnum];
    int col=cl->pos[2*cl->agnum+1];
    row+=dr;
    col+=dc;
    while (inbounds(cl->map, row, col)) {
        if (isagent(cl, row, col)) {
	    return true;
	}
        if (cl->map->grid[row][col]=='#') {
	    return false;
	}
	row+=dr;
	col+=dc;
    }
    return false;  
}

bool blockedbyagent(clientinfo_t* cl, int dr, int dc) {
    int row=cl->pos[2*cl->agnum];
    int col=cl->pos[2*cl->agnum+1];
    row+=dr;
    col+=dc;
    if (isagent(cl, row, col)) {
	return true;
    }
    return false;  
}
