/* timer2.h
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * We set up timer 2 to give us an interrupt
 * every millisecond. Tasks that have to occur
 * regularly (every few milliseconds) can be added 
 * to the interrupt handler (in timer2.c) or can
 * be added to the main event loop that checks the
 * clock tick value. This value (32 bits) can be 
 * obtained using the get_clock_ticks() function.
 */

#ifndef TIMER2_H_
#define TIMER2_H_

#include <stdint.h>

/* Declare TIMER_FUNCTION to be a type of function that takes
 * no arguments and returns nothing. A pointer to a function of
 * this type can be provided to the init_timer2() as a function 
 * that will be called on each timer interrupt. A NULL pointer
 * means no function is called.
 */
typedef void TIMER_FUNCTION(void);

/* Set up our timer to give us an interrupt every millisecond
 * and call the provider timer function timer_func. No function
 * is called if a null pointer is provided. Note that the provided
 * function will be called whilst interrupts are still disabled so
 * the function contents should be kept brief, or interrupts
 * re-enabled first. If enabled, interrupts can be left enabled - this function
 * is the last thing called by the interrupt handler.
 */
void init_timer2(TIMER_FUNCTION* timer_func);

/* Return the current clock tick value */
uint32_t get_clock_ticks(void);

#endif	/* TIMER2_H_ */
