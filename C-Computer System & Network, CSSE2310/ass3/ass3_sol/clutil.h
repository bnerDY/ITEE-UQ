#ifndef CLUTIL_H
#define CLUTIL_H

#include <stdbool.h>
#include "types.h"
#include "map.h"

#define CL_GOAL 0
#define CL_PCOUNT 1
#define CL_PVALS 2
#define CL_DEPS 3
#define CL_COM 4

#define DONE(X) print_cmsg(X);return X;

typedef struct {
    uint agcount;
    char* chars;
    uint agnum;	// which agent we are    
    map_t* map;
    uint* pos;	// this will be updated on each move
} clientinfo_t;


void free_client(clientinfo_t* c);

// Need to read, the total number of agents, line of chars, number of this agent, map dims, map
clientinfo_t* setup();
bool getTurn(clientinfo_t* client);
void print_cmsg(int e);
char getmove(uint r1, uint c1, uint r2, uint c2);

// sets the direction to 90 right of current
void rotateright(int* dr, int* dc);
bool isagent(clientinfo_t* cl, int row, int col);
void wallmove(clientinfo_t* c, int* dr, int* dc);
bool sightonagent(clientinfo_t* cl, int dr, int dc, uint agid);
bool blockedbyagent(clientinfo_t* cl, int dr, int dc);



#endif