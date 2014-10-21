/*
 * score.h
 * 
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#ifndef SCORE_H_
#define SCORE_H_

#include <stdint.h>

void init_score(void);
void add_to_score(uint16_t value);
uint16_t get_score(void);
void init_row(void);
void add_to_row(uint16_t value);
uint16_t get_row(void);
#endif /* SCORE_H_ */

