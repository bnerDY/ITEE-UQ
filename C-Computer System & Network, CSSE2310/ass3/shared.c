#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>

typedef struct {
    int r;
    int c;
    char **grid;
} Map;
/*Map struct m*/
Map m;

/*Alloc the memeor to the map.*/
char** alloc_map(int row, int col) {
    int i;
    char** map = malloc(sizeof(char*)*row);
    for (i = 0; i < row; ++i) {
        map[i] = malloc(sizeof(char)*col);
    }
    return map;
}
