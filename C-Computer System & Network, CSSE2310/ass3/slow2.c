#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "shared.h"
#include <unistd.h>
#include <sys/types.h>
/*Total number of agent, current agent, agentlist.*/
int tot = 0;
/*current agent*/
int current = 0;
/*agent list*/
char* agentlist;
/*Struct for Map.*/
typedef struct {
    int r;
    int c;
    char **grid;
} Map;
/*Map struct m*/
Map m;
/*Record the position.*/
typedef struct {
    int x;
    int y;
} Position;

int main(int argc, char** argv) {
    char buffer[100];
    int i, j;
    /*Read data from stdin*/
    fgets(buffer, 1000, stdin);
    tot = atoi(&buffer[0]);
    fgets(buffer, 1000, stdin);
    agentlist = buffer;
    agentlist[tot] = '\0';
    fgets(buffer, 1000, stdin);
    current = atoi(&buffer[0]);
    fgets(buffer, 1000, stdin);
    sscanf(buffer, "%d %d", &m.r, &m.c);
    /*malloc the memeory, function from shared.c*/
    m.grid = alloc_map(m.r, m.c);
    for (i = 0; i < m.r; i++) {
        char* line = fgets(buffer, 100, stdin);
        for (j = 0; j < m.c; j++) {
            m.grid[i][j] = line[j];
        }
    }
}
