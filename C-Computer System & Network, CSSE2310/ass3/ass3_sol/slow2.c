#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "clutil.h"

// cheap and nasty solution for the slow2 behaviour

int main(int argc, char** argv) {
    int prevr=-2, prevc=-2;
    int followid=-1;	// which agent are we following?
    if (argc!=1) {  
        DONE(CL_PCOUNT);
    }
    clientinfo_t* client=setup();
    if (!client) {
        DONE(CL_COM);
    }
    // now check to see if there is a + agent
    for (uint i=0;i<client->agcount;++i) {
        if (client->chars[i]=='+') {
	    followid=i;
	    break;
	}
    }
    if (followid==-1) {
        DONE(CL_DEPS);
    }
    char* moves=malloc(sizeof(char)*10);
    uint cap=10;
    uint wpos=0;
    uint rpos=0;
        // recording phase
     while (getTurn(client)) {
        if (prevr==-2) {	// no previous record
	    prevr=client->pos[followid*2];
	    prevc=client->pos[followid*2+1];
	    printf("H\n");
	    fflush(stdout);   	    
	    continue;
	}
	if (wpos==cap-1) {
	    cap+=10;
	    moves=realloc(moves,cap);
	}
//fprintf(stderr, "%d %d -> %d %d\n", 	prevr, prevc, client->pos[followid*2], client->pos[followid*2+1]);
	char m=getmove(prevr, prevc, client->pos[followid*2], client->pos[followid*2+1]);

	if (m=='H') {
	   // switch to playback
	   if (wpos==0) {
	       DONE(CL_GOAL);
	   }
	   printf("%c\n", moves[0]);
	   rpos=1;
	   fflush(stdout);
	   break;
	} else {
	    moves[wpos]=m;
	    prevr=client->pos[followid*2];
	    prevc=client->pos[followid*2+1];	      
	    wpos++;	  
	    printf("H\n");
	    fflush(stdout);      
	}
    }
        // now we need to know if we broke due to finding an H or by eof
    if (rpos==0) {
	free_client(client);
	DONE(CL_COM);            
    }
    
    while (getTurn(client)) {
        // we don't care what the other agent does at this point
        printf("%c\n",moves[rpos++]);
	fflush(stdout);
        if (rpos==wpos) {
	    // we need to get another move
	    getTurn(client);
	    free_client(client);
	    DONE(CL_GOAL);
	}
    }
         // if we arrived here, we lost the handler
    free_client(client);
    DONE(CL_COM);
}
