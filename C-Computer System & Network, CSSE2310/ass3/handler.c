#include <stdio.h>   /* printf, stderr, fprintf */
#include <sys/types.h> /* pid_t */
#include <unistd.h>  /* _exit, fork */
#include <stdlib.h>  /* exit */
#include <errno.h>   /* errno */
#include <string.h>
#include <ctype.h>
#include "shared.h"

/* Basic definition for errors.*/
#define ERROR_ARGS 1
#define ERROR_STEP 2
#define ERROR_FILE 3
#define ERROR_MAPF 4
#define ERROR_AGEN 5
#define ERROR_AGEF 6
#define ERROR_RUNA 7
/*The limit for int*/
#define INT_MAX 32767
/*Map struct*/
typedef struct {
    int nRows, nCols;
    char **grid;
} Map;
/*Map struct m*/
Map m;
/*Store infomation for agent*/
typedef struct {
    int x;
    int y;
    char n;
    char aProcess[20];
    char arg[30];
} Agent;
/*Agemt struct A*/
Agent a[100];
/*agent count for sucess read agent*/
int agentCount = 0;
/*map file name*/
char *mapFileName;
/*agent file name*/
char *agentFileName;
/*max steps for input*/
int maxsteps;
/*File map*/
FILE *map;
/*File agent*/
FILE *agent;

/*Show the agent in the map.*/
void show_agent() {
    int i, j, k;
    for(i = 0; i < m.nRows; i++) {
        for(j = 0; j < m.nCols; j++) {
            if(m.grid[i][j] == '#') {
                printf("#");
            } else {
                for(k = 0; k < agentCount; k++) {
                    if(a[k].x - 1 == i && 
                            a[k].y - 1 == j) {
                        printf("%c", a[k].n);
                        break;
                    }
                }
                if(k == agentCount) {
                    printf(".");
                }
            }
        }
        printf("\n");
    }
    printf("\n");
}

/*Error for handler*/
void error(int code, char* arg) {
    switch(code) {
        case 1:
            fprintf(stderr, "Usage: handler mapfile maxsteps agentfile\n");
            break;
        case 2:
            fprintf(stderr, "Invalid maxsteps.\n");
            break;
        case 3:
            fprintf(stderr, "Unable to open map file.\n");
            break;
        case 4:
            fprintf(stderr, "Corrupt map.\n");
            break;
        case 5:
            fprintf(stderr, "Unable to open agent file.\n");
            break;
        case 6:
            fprintf(stderr, "Corrupt agents.\n");
            break;
        case 7:
            fprintf(stderr, "Error running agent.\n");
            break;
        case 8:
            fprintf(stderr, "Agent %s walled.\n", arg);
            break;
        case 9:
            fprintf(stderr, "Agent %s collided.\n", arg);
            break;
        case 10:
            fprintf(stderr, "Too many steps.\n");
            break;
    }
    exit(code);
}

/*Check row of map file*/
void check_row(int r) {
    int c;
    for (c = 0; c < m.nCols; ++c) {
        char p = m.grid[r][c];
        if (!(p == '.' || p == '#')) {
            error(4, "");
        }
    }
}

/*Check the map file*/
void check_map_file(char* mapF, char* mode) {
    char d, buffer[10];
    map = fopen(mapF, mode);
    int r;
    if(!map) {
	error(3, "");
    }
    if (fgets(buffer, 10, map) == NULL) {
        error(4, "");
    }
    if (sscanf(buffer, "%d %d%1[^\n]\n", &m.nRows, &m.nCols, &d) != 2 ||
            m.nRows < 1 || m.nRows > 999 || m.nCols < 1 || m.nCols > 999) {
        error(4, "");
    }
    /*Alloc the memory to map*/
    m.grid = malloc(m.nRows * sizeof(char *));
    for (r = 0; r < m.nRows; ++r) {
        m.grid[r] = calloc(m.nCols + 2, sizeof(char));
        if (fgets(m.grid[r], m.nCols + 2, map) == NULL ||
                m.grid[r][m.nCols] != '\n') {
            error(4, "");
        }
        check_row(r);
    }
    if (fgetc(map) != EOF) {
        error(4, "");
    }
    fclose(map);
}

/*Check the agent file*/
void check_agent_file(char* agentF, char* mode) {
    char buffer[80];
    int xpos, ypos;
    char achar;
    char aprocess[20];
    char argument[30];
    int res, i;
    agent = fopen(agentF, mode);
    if(!agent) {
        error(5, "");
    }
    while (fgets(buffer, 80, agent) != NULL) {
        if (buffer[0] != '#') {
            res = sscanf(buffer, "%d %d %c %s %s", &xpos, &ypos, &achar,
                    aprocess, argument);
            if (res < 4 || res > 5) {
                error(7, "");
            }
            /*Check to see if the agent can run or not.*/
            if ((strcmp(aprocess, "./simple") != 0) &&
                    (strcmp(aprocess, "./slow") != 0) &&
                    (strcmp(aprocess, "./slow1") != 0) &&
                    (strcmp(aprocess, "./waller") != 0) &&
                    (strcmp(aprocess, "./fromfile") != 0)) {
                error(7, "");
            }
            agentCount++;
            /*Store information for each agent*/
            for (i = 0; i < agentCount; i++) {
                a[i].x = xpos;
                a[i].y = ypos;
                a[i].n = achar;
                strcpy(a[i].aProcess, aprocess);
                strcpy(a[i].arg, argument);
            }
        }
    }
    /*Check if no agent at all*/
    if (agentCount == 0) {
        error(6, "");
    }
}


/*Process the input arguments*/
int process_command_line(int argc, char* argv[]) {
    mapFileName = argv[1];
    agentFileName = argv[3];
    char* res;
    if(argc > 0) {
        if(argc == 4) {
            res = argv[2];
            maxsteps = atoi(res);
            /*Check the maxsteps*/
            if (maxsteps >= INT_MAX || maxsteps <= 0) {
                error(2, "");
            }
        } else {
            error(1, "");
        }
    }
    return maxsteps;
}

int main(int argc, char* argv[]) {
    int i, j, pid, turn;
    maxsteps = process_command_line(argc, argv);
    check_map_file(mapFileName, "r");
    check_agent_file(agentFileName, "r");
    int pipeIn[80][2], pipeOut[80][2];
    FILE *pr[80], *pw[80];
    for (i = 0; i < agentCount; i++) {
        pipe(pipeIn[i]);
        pipe(pipeOut[i]);
        if ((pid = fork()) == 0) { /*child process*/
            close(pipeIn[i][0]);
            close(pipeOut[i][1]);
            dup2(pipeIn[i][1], 1);
            dup2(pipeOut[i][0], 0);
            /*Exec the agent.*/
            if (execlp(a[i].aProcess, a[i].aProcess, a[i].arg, NULL) < 0) {
                error(7, "");
            }
        } else if(pid < 0) {
            fprintf(stderr, "Fork error\n.");
            exit(20);
        } else {
            /*parent process*/
            close(pipeIn[i][1]);
            close(pipeOut[i][0]);
            /*Parent Send data*/
            pr[i] = fdopen(pipeIn[i][0], "r");
            pw[i] = fdopen(pipeOut[i][1], "w");
            fprintf(pw[i], "%d\n", agentCount);
            fflush(pw[i]);
            for (j = 0; j < agentCount; j++) {
                fprintf(pw[i], "%c", a[i].n);
                fflush(pw[i]);
            }
            fprintf(pw[i], "\n");
            fflush(pw[i]);
            fprintf(pw[i], "%d\n", i+1);
            fflush(pw[i]);
            fprintf(pw[i], "%d %d\n", m.nRows, m.nCols);
            fflush(pw[i]);
            for(j = 0; j < m.nRows; j++) {
                fprintf(pw[i], "%s", m.grid[j]);
                fflush(pw[i]);
            }
            /*Unchanged data send over.*/
        }
    }
    for (turn = 0; turn < maxsteps; turn++) {
        for (i = 0; i < agentCount; i++) {
            /*Send the update position.*/
            for(j = 0; j < agentCount; j++) {
                fprintf(pw[i], "%d %d\n", a[j].x, a[j].y);
            }
            fflush(pw[i]);
            char res[100];
            fscanf(pr[i], "%s", res);
            if(!strcmp(res, "D")) {
                printf("Agent %c succeeded.\n", a[i].n);
                exit(0);
            } else if(res[0] == '7') {
                error(7, "");
            } else if(res[0] == '8') {
                error(8, "");
            } else if(res[0] == '9') {
                error(9, "");
            } else if(!strcmp(res, "Incorrect number of params.")) {
                fprintf(stderr, "Agent %c exited with status 1.\n", a[i].n);
                exit(12);
            } else if(!strcmp(res, "Invalid params.")) {
                fprintf(stderr, "Agent %c exited with status 2.\n", a[i].n);
                exit(12);
            } else if(!strcmp(res, "Dependencies not met.")) {
                fprintf(stderr, "Agent %c exited with status 3.\n", a[i].n);
                exit(12);
            }
			/*Make new position from the given direction.*/
            if(res[0] == 'E') {
                a[i].y++;
            } else if(res[0]=='S') {
                a[i].x++;
            } else if(res[0]=='W') {
                a[i].y--;
            } else if(res[0]=='N') {
                a[i].x--;
            }
            show_agent();
        }
    }
    /*If it is not over means too many steps*/
    error(10, "");
    return 0;
}

