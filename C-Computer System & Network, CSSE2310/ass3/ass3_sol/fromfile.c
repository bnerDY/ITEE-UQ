#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "map.h"
#include "util.h"
#include "types.h"
#include "clutil.h"


int main(int argc, char** argv) {
    int dr, dc;
    if (argc!=2) {
        DONE(CL_PCOUNT);
    }
    FILE* file=fopen(argv[1],"r");
    if (!file) {  
        DONE(CL_PVALS);
    }
    clientinfo_t* client=setup();
    if (!client) {
        DONE(CL_COM);
    }
    while (getTurn(client)) {
        int c=fgetc(file);
	if (c==EOF) {
	    free_client(client);
	    DONE(CL_GOAL);
            fflush(stdout);
	}
	if (c=='H') {	// This can never be unsafe
	    printf("H\n");
	    fflush(stdout);
	    continue;
	}
	switch ((char)c) {
	  case 'N':
	  case 'S':
	  case 'E':
	  case 'W':
	      break;
	  default:
	      printf("H\n");
	      fflush(stdout);
	      continue;
	}
	getDirVector(c, &dr, &dc);
	int nr=client->pos[2*client->agnum]+dr;
	int nc=client->pos[2*client->agnum+1]+dc;
	if ((nr>=0) && (nr<=client->map->h) && (nc>=0) && (nc<=client->map->w)
	      && (client->map->grid[nr][nc]==' ')) {
	    printf("%c\n", c);
	} else {
	    printf("H\n");
	}
	fflush(stdout);	    
    }
        // if we arrived here, we lost the handler
    free_client(client);
    DONE(CL_COM);
}
