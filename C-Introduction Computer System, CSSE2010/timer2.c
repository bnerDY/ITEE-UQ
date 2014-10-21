/* timer2.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * We setup timer2 to generate an interrupt every 1ms
 * We update a global clock tick variable - whose value
 * can be retrieved using the get_clock_ticks() function.
 */

#ifdef AVR
#include <avr/io.h>
#include <avr/interrupt.h>
#else
#include <sys/time.h>
#endif

#include "timer2.h"

/* Our internal clock tick count - incremented every 
 * millisecond. Will overflow every ~49 days. */
static volatile uint32_t clockTicks;

/* A pointer to our user-provided timer function that will
 * be called during every interrupt.
 */
static TIMER_FUNCTION *user_provided_timer_function;

/* Set up timer 2 to generate an interrupt every 1ms. 
 * We will divide the clock by 64 and count up to 124.
 * We will therefore get an interrupt every 64 x 125
 * clock cycles, i.e. every 1 milliseconds with an 8MHz
 * clock. 
 * The counter will be reset to 0 when it reaches it's
 * output compare value.
 */
void init_timer2(TIMER_FUNCTION* timer_func) {
	/* Reset clock tick count. L indicates a long (32 bit) 
	 * constant. 
	 */
	clockTicks = 0L;
	
	/* Remember the user provider timer function */
	user_provided_timer_function = timer_func;
#ifdef AVR
	/* Set the output compare value to be 124 */
	OCR2 = 124;

	/* Enable an interrupt on output compare match. 
	 * Note that interrupts have to be enabled globally
	 * before the interrupts will fire.
	 */
	TIMSK |= (1<<OCIE2);

	/* Set the timer to clear on compare match (CTC mode)
	 * and to divide the clock by 64. This starts the timer
	 * running.
	 */
	TCCR2 = (1<<WGM21)|(0<<WGM20)|(0<<CS22)|(1<<CS21)|(1<<CS20);
#endif
}

uint32_t get_clock_ticks(void) {
#ifdef AVR
	uint32_t returnValue;

	/* Disable interrupts so we can be sure that the interrupt
	 * doesn't fire when we've copied just a couple of bytes
	 * of the value. Interrupts are re-enabled if they were
	 * enabled at the start.
	 */
	uint8_t interruptsOn = bit_is_set(SREG, SREG_I);
	cli();
	returnValue = clockTicks;
	if(interruptsOn) {
		sei();
	}
	return returnValue;
#else
/* Non AVR - we just get the current of day and turn it in 
 * to milliseconds. Problems may happen if this overflows
 * during the course of program execution. We ignore this.
 */
	struct timeval t;
	gettimeofday(&t, 0);
	return (uint32_t)(t.tv_sec * 1000 + t.tv_usec / 1000);
#endif
}

#ifdef AVR
ISR(TIMER2_COMP_vect) {
	/* Increment our clock tick count */
	clockTicks++;
	
	/* If we have a user provided timer function, call it now */
	if(user_provided_timer_function) {
		user_provided_timer_function();
	}
}
#endif
