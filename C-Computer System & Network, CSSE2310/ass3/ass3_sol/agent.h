#ifndef AGENT_H
#define AGENT_H
#include <stdio.h>
#include <unistd.h>	// for pid_t
#include "types.h"
#include <stdbool.h>

typedef struct {
    char s;	// char representing agent
    uint r;	// row
    uint c;	// column
    uint argc;
    char** argv;
    char* args;	// holds all params
    FILE* to;	// pipe ends to agent
    FILE* from;
    pid_t pid;	// pid of agent
    bool alive;
    int status;
} agent_t;

void cleanup_agent(agent_t* a);
bool init_agent(agent_t* a, const char* line);

// returns an agent* array and sends back the number of records
agent_t** read_agentfile(FILE* f, uint* numrecords);


#endif