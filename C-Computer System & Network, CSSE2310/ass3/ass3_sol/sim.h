#ifndef SIM_H
#define SIM_H

#include "agent.h"
#include "map.h"

/* data structure which describes whole simulation */
typedef struct {
    uint agcount;
    agent_t** ags;
    map_t* map;
} sim_t;

int init_sim(sim_t* sim, char* mapfile, char* agentfile);
int run_sim(sim_t* sim, uint maxs);
bool start_agents(sim_t* sim);

agent_t* find_agent(sim_t* sim, pid_t pid);


// preferably do not call until children are set to auto-reap
void endchildren(sim_t* sim);

#endif