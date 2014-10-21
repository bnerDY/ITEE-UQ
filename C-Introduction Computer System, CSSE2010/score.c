/*
 * score.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#include "score.h"

uint16_t score;
uint16_t number;

void init_score(void) {
	score = 0;
}

void add_to_score(uint16_t value) {
	score += value;
}

uint16_t get_score(void) {
	return score;
}


void init_row(){
    number = 0;
}

void add_to_row(uint16_t value){
    number += value;
}

uint16_t get_row(void){
    return number;
}
