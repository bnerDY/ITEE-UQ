/*
 * external_interrupt.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */ 

#ifdef AVR
#include <avr/io.h>
#include <avr/interrupt.h>
#endif
#include "external_interrupt.h"

/* The last button pushed. Can be overwritten if button_pushed() is not called
 * before the next button push happens. Value will be 0 to 3 if a button is pushed.
 */
static volatile int8_t last_button_pushed = -1;

void init_external_interrupts(void) {
#ifdef AVR
	/* Enable the required external interrupts 0 to 3. We set up 
	 * each interrupt to look for a falling edge event.
	 * Global interrupts will also need to be enabled (not done here).
	 */
	EICRA = (1<<ISC31)|(0<<ISC30)|
			(1<<ISC21)|(0<<ISC20)|
			(1<<ISC11)|(0<<ISC10)|
			(1<<ISC01)|(0<<ISC00);
	EIMSK = (1<<INT0)|(1<<INT1)|(1<<INT2)|(1<<INT3);
	EIFR = (1<<INTF0)|(1<<INTF1)|(1<<INTF2)|(1<<INTF3);	

#endif
	last_button_pushed = -1;
}

int8_t button_pushed() {
#ifdef AVR
	int8_t interrupts_enabled;
	int8_t button;
	
	/* Turn off interrupts (if enabled) while we access last_button_pushed */
	interrupts_enabled = bit_is_set(SREG, SREG_I);
	cli();
	button = last_button_pushed;
	last_button_pushed = -1;
	/* If interrupts were on, turn them back on */
	if(interrupts_enabled) {
		sei();
	}
	return button;
#else
	/* Non AVR - buttons can never be pressed */
	return -1;
#endif
}

#ifdef AVR
/* Called from our button interrupt handlers. (We know that interrupts
 * are disabled when we get here.) We record the button that has been pushed.
 */
static void handle_button_push(int8_t button) {
	last_button_pushed = button;
}

/* Interrupt handler for button 0 button push */
ISR(INT0_vect) {
	handle_button_push(0);
}

/* Interrupt handler for button 1 button push */
ISR(INT1_vect) {
	handle_button_push(1);
}

/* Interrupt handler for button 2 button push */
ISR(INT2_vect) {
	handle_button_push(2);
}

/* Interrupt handler for button 3 button push */
ISR(INT3_vect) {
	handle_button_push(3);
}
#endif
