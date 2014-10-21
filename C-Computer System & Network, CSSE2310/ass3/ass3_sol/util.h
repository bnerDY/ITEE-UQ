#ifndef UTIL_H
#define UTIL_H
#include <stdio.h>
#include <stdbool.h>
#include "types.h"


// This code might deallocate the buffer you pass it
// lim will be updated to reflect new capacity
// returns true if valid line, false on no line available.
bool get_noncomment_line(char** buffer, uint* lim, FILE* f);

void getDirVector(char d, int* deltarow, int* deltacol);

#endif