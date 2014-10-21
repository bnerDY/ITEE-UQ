#include <stdio.h>
#include "errs.h"

/* Yes I could have used variable arguments here but I prefer to have arguments checked */
void print_msg(int m, char c, int d) {
    switch (m) {
    case SUCCESS: printf("Agent %c succeeded.\n", c); fflush(stdout);break;
    case ERR_ARGS: fprintf(stderr, "%s\n", "Usage: handler mapfile maxsteps agentfile"); break;
    case ERR_MAX: fprintf(stderr, "%s\n", "Invalid maxsteps."); break;
    case ERR_MAPFILE: fprintf(stderr, "%s\n", "Unable to open map file."); break;
    case ERR_MAPREAD: fprintf(stderr, "%s\n", "Corrupt map."); break;
    case ERR_AGFILE: fprintf(stderr, "%s\n", "Unable to open agent file."); break;
    case ERR_AGREAD: fprintf(stderr, "%s\n", "Corrupt agents."); break;    
    case ERR_START: fprintf(stderr, "%s\n", "Error running agent."); break;
    case ERR_WALL: fprintf(stderr, "Agent %c walled.\n",c); break;
    case ERR_CRASH: fprintf(stderr, "Agent %c collided.\n",c); break;
    case ERR_STEPS: fprintf(stderr, "Too many steps.\n"); break;
    case ERR_REPLY: fprintf(stderr, "Agent %c sent invalid response.\n",c); break;
    case ERR_CLOSE: fprintf(stderr, "Agent %c exited with status %d.\n",c,d); break;
    case ERR_SIGCLOSE: fprintf(stderr, "Agent %c exited due to signal %d.\n",c,d); break;
    case ERR_SIGINT: fprintf(stderr, "Exiting due to INT signal.\n"); break;    
    default: fprintf(stderr, "%s (%d)\n", "Something really weird happened.", m);
    }
}
