#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "map.h"
#include "util.h"
#include "types.h"
#include "clutil.h"


int main(int argc, char** argv) {
    int dr, dc;
    char dir;
    if (argc!=2) {
        DONE(CL_PCOUNT);
    }
    if (strlen(argv[1])!=1) {
        DONE(CL_PVALS);
    }
    dir=argv[1][0];
    if ((dir!='N') && (dir!='S') && (dir!='E') && (dir!='W')) {
        DONE(CL_PVALS);
    }
    getDirVector(dir, &dr, &dc);
    clientinfo_t* client=setup();
    if (!client) {
        DONE(CL_COM);
    }
    int turnstaken=0;
    while (getTurn(client)) {      
	if (turnstaken==10) {
	    free_client(client);
	    DONE(CL_GOAL);
	} else {
	    int nr=client->pos[2*client->agnum]+dr;
	    int nc=client->pos[2*client->agnum+1]+dc;
	    if ((nr>=0) && (nr<client->map->h) && (nc>=0) && (nc<client->map->w)
	          && (client->map->grid[nr][nc]==' ')) {
	        printf("%c\n", dir);
	    } else {
	        printf("H\n");
	    }
	    fflush(stdout);
	}
	turnstaken++;
    }
        // if we arrived here, we lost the handler
    free_client(client);
    DONE(CL_COM);
}
