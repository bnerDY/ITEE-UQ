#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "clutil.h"



int main(int argc, char** argv) {
    int prevr, prevc;
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
    char moves[11];
    short wpos=0, rpos=0;	// The idea is that we use moves as a circular buffer
    if (!getTurn(client)) {
	free_client(client);
	DONE(CL_COM);
    }
       // record the initial position of the +agent
    prevr=client->pos[followid*2];
    prevc=client->pos[followid*2+1];
      // We'll hold here until we see the agent move
    printf("H\n");
    fflush(stdout);
        // Now load the buffer with 10 moves
        //    
    for (;wpos<10;wpos++) {
        if (!getTurn(client)) {
            free_client(client);
	    DONE(CL_COM);
	}
	moves[wpos]=getmove(prevr, prevc, client->pos[followid*2], client->pos[followid*2+1]);
        prevr=client->pos[followid*2];
        prevc=client->pos[followid*2+1];	
        printf("H\n");
        fflush(stdout);	
    }
        // ok now we have a full queue of moves
    while (getTurn(client)) {
        moves[wpos]=getmove(prevr, prevc, client->pos[followid*2], client->pos[followid*2+1]);
        prevr=client->pos[followid*2];
        prevc=client->pos[followid*2+1];	      
        wpos=(wpos+1)%11;
        printf("%c\n", moves[rpos]);
	rpos=(rpos+1)%11;
	fflush(stdout);      
    }
        // if we arrived here, we lost the handler
    free_client(client);
    DONE(CL_COM);
}
