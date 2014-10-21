#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "shared.h"
#include <unistd.h>
#include <sys/types.h>
/*Total number of agent, current agent, agentlist*/
int tot = 0;
/*current agent*/
int current = 0;
/*agent list*/
char* agentlist;
/*Struct for map.*/
typedef struct {
    int r;
    int c;
    char **grid;
} Map;
/*Map struct m*/
Map m;
/*Used for store position*/
typedef struct {
    int x;
    int y;
} Position;

int main(int argc, char** argv) {
    char buffer[100];
    int i, j;
    int count;
    if (argc != 2) {
        fprintf(stdout, "Incorrect number of params.\n");
        exit(1);
    }
    char dir = argv[1][0];
    if(!(dir=='E' || dir=='W' || dir=='S' || dir=='N')) {
        fprintf(stdout, "Invalid params.\n");
        exit(2);
    }
    /*Read data from stdin ( line by line )*/
    fgets(buffer, 1000, stdin);
    tot = atoi(&buffer[0]);
    fgets(buffer, 1000, stdin);
    agentlist = buffer;
    agentlist[tot] = '\0';
    fgets(buffer, 1000, stdin);
    current = atoi(&buffer[0]);
    fgets(buffer, 1000, stdin);
    sscanf(buffer, "%d %d", &m.r, &m.c);
    m.grid = alloc_map(m.r, m.c);
    for (i = 0; i < m.r; i++) {
        char* line = fgets(buffer, 100, stdin);
        for (j = 0; j < m.c; j++) {
            m.grid[i][j] = line[j];
        }
    }
    /*Process the input data.*/
    for (count = 0; count < 11; count++) {
        Position pos[tot];
        for (i = 0; i < tot; i++) {
            int x, y;
            char* line = fgets(buffer, 1000, stdin);
            if (line == NULL) {
                fprintf(stdout, "Handler communication breakdown.\n");
                exit(4);
            }
            sscanf(line, "%d %d", &x, &y);
            pos[i].x = x-1;
            pos[i].y = y-1;
        }
        Position currentP = pos[current - 1];
        /*Simple run 10 times then exit.*/
        if (count == 10) {
            fprintf(stdout, "D\n");
            fflush(stdout);
            exit(0);
        }
        if (dir == 'N') {
            if (currentP.x-1 < 0 ||
                    m.grid[currentP.x-1][currentP.y] == '#') {
                fprintf(stdout, "H\n");
                fflush(stdout);
            } else {
                fprintf(stdout, "N\n");
                fflush(stdout);
            }
        } else if (dir == 'E') {
            if (currentP.y+1 == m.c||
                    m.grid[currentP.x][currentP.y+1] == '#') {
                fprintf(stdout, "H\n");
                fflush(stdout);
            } else {
                fprintf(stdout, "E\n");
                fflush(stdout);
            }
        } else if (dir == 'W') {
            if (currentP.y-1 < 0||
                    m.grid[currentP.x][currentP.y-1] == '#') {
                fprintf(stdout, "H\n");
                fflush(stdout);
            } else {
                fprintf(stdout, "W\n");
                fflush(stdout);
            }
        } else if (dir == 'S') {
            if (currentP.x+1 == m.r||
                    m.grid[currentP.x+1][currentP.y] == '#') {
                fprintf(stdout, "H\n");
                fflush(stdout);
            } else {
                fprintf(stdout, "S\n");
                fflush(stdout);
            }
        }
    }
}
