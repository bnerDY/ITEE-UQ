#ifndef MAP_H
#define MAP_H
#include <stdbool.h>

#include "types.h"

/* to make it clear why I am checking things */
#define CWAL '#'
#define COPEN '.'
#define CAGOP ' '




typedef struct {
    uint h,w;    /* dimenions of map */
    uint steps;
    char** grid; // not strings
} map_t;


map_t* alloc_map(uint h, uint w);
void free_map(map_t* m);
int fill_grid(map_t* m, FILE* f, char spchar);
void show_map(map_t* t);

bool inbounds(map_t* m, int r, int c);

#endif