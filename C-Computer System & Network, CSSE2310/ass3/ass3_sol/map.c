#include <stdio.h>
#include <stdlib.h>
#include "errs.h"
#include "map.h"


map_t* alloc_map(uint h, uint w) {
    map_t* res=malloc(sizeof(map_t));
    res->h=h;
    res->w=w;
    res->grid=malloc(sizeof(char*)*h);
    for (uint i=0;i<h;++i) {
        res->grid[i]=malloc(sizeof(char)*w);
    }
    return res;
}

void free_map(map_t* m) {
    for (uint i=0;i<m->h;++i) {
        free(m->grid[i]);
    }
    free(m->grid);
    free(m);
}

/* Assumes the dimensions of the map have successfully been read */
/* Why doesn't this code use fgets? Because I'm being super paranoid */
int fill_grid(map_t* m, FILE* f, char spchar) {
    char ch;
    for (uint r=0;r<m->h;++r) {
        for (uint c=0;c<m->w;++c) {
	    ch=(char)fgetc(f);
	    if (feof(f) || ((ch!=spchar) && (ch!='#'))) {
	        return ERR_MAPREAD;
	    }
	    m->grid[r][c]=ch;
	}
	ch=(char)fgetc(f);
	if (feof(f) || (ch!='\n')) {
	    return ERR_MAPREAD;
	}	// read a correct line at this point
    } 
    return 0;
}

void show_map(map_t* map) {
    for (uint r=0;r<map->h;++r) {
        for (uint c=0;c<map->w;++c) {
	    putchar(map->grid[r][c]);  
	}
	putchar('\n');
    }
    putchar('\n');
}

bool inbounds(map_t* m, int r, int c) {
    return (r>=0) && (r<m->h) && (c>=0) && (c<m->w); 
}
