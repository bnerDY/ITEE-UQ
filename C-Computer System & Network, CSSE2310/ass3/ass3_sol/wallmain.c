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
    while (getTurn(client)) {
        wallmove(client, &dr, &dc);
    }
        // if we arrived here, we lost the handler
    free_client(client);
    DONE(CL_COM);
}
