/*
 * keypad.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */ 

#include <stdint.h>
#ifdef AVR
#include <avr/io.h>
#include <avr/interrupt.h>
#else
#include <unistd.h>
#endif
#include "keypad.h"

#include <stdio.h>

/* Last button pushed - initially NULL. */
static char last_button_pushed = 0;

#ifdef AVR
/* Symbol table - 2D array which we can reference with [row][col] to
 * determine the button that has been pushed. Rows are 1 to 4, columns
 * are 1 to 4. Row 0 and column 0 values are irrelevant and won't be used
 * so we just make those null characters.
 */
static char keypad_buttons[5][5] = 
		{	{0,   0,   0,   0,   0 },
			{0,  '1', '2', '3', 'A'},
			{0,  '4', '5', '6', 'B'},
			{0,  '7', '8', '9', 'C'},
			{0,  '0', 'F', 'E', 'D'}
		};

/* Variable which indicates which column is live (column line pulled low).
 * We start with no columns pulled low.
 */
static uint8_t active_column;

/* Port E output values when checking each column. Indexed by column 1 to 4, index 0 is
 * irrelevant. We output a 0 to the specific column and a 1 to the others. Lower 4 bits are
 * irrelevant - these bits are used for RS232 communications. Connections are as follows:
 * Column 1 - PMOD pin 4 - port E, pin 5
 * Column 2 - PMOD pin 3 - port E, pin 7
 * Column 3 - PMOD pin 2 - port E, pin 4
 * Column 4 - PMOD pin 1 - port E, pin 6
 */
static uint8_t portE_column_strobe_values[5] = 
		{	0,	/* index 0 is irrelevant - this is padding */
			0xD0,	/* 11010000 */
			0x70,	/* 01110000 */
			0xE0,	/* 11100000 */
			0xB0	/* 10110000 */			
		};
#endif
		
/* A pointer to the handler we'll call when a button is pushed. The user supplies
 * the handler when this module is initialised.
 */
static BUTTONHANDLER* button_handler;

/* See documentation in .h file
 */
void init_keypad(BUTTONHANDLER* handler_function_ptr) {
#ifdef AVR
	/* Make the upper 4 bits of port E be outputs.
	 * These are our column lines. We initially drive them
	 * all as 1's. The lower 4 bits are left as inputs.
	 */
	DDRE = 0xF0;
	PORTE = 0xF0;

	/* No column is "active" initially. */
	active_column = 0;
#endif
	
	/* Store the pointer to the handler we're going to call */
	button_handler = handler_function_ptr;
	
	/* No last button pushed */
	last_button_pushed = 0;
}

/* See documentation in .h file.
 */
void check_keypad_column(void) {
#ifdef AVR
	/* The button state (old row values) is preserved between calls to this function 
	 * so that we can keep track of changes to the state - i.e. when a button is 
	 * pressed that wasn't previously.
	 */
	static uint8_t row_value_old[5];	/* Indexed by column*/
	uint8_t row, row_value, button_pushed;
	
	if(active_column > 0) {
		/* We had an active column - check all the rows to see 
		 * if any buttons were pushed. One of the upper 4 bits
		 * will be 0 if a button has been pushed. (More than one
		 * may be 0 if more than one button has been pushed, but
		 * support for multiple simultaneous button pushes is
		 * not provided.)
		 */
		row_value = ((PIND & (1<<6))>>2) |	/* Pin D6 (row 4) as bit 4 */
				((PINB & (1<<5))>>2) |		/* Pin B5 (row 3) as bit 3 */
				((PIND & (1<<4))>>2) |		/* Pin D4 (row 2) as bit 2 */
				((PINB & (1<<6))>>5);		/* Pin B6 (row 1) as bit 1 */
		if(row_value != row_value_old[active_column]) {
			/* State has changed */
			row_value_old[active_column] = row_value;
			if(row_value != 0x1E) {
				/* Button has been pushed - work out which one. */
				for(row = 1; row <= 4; row++) {
					/* Extract whether the button is pushed in the given row.
					 * Row 1 is bit 1, row 2 is bit 2, row 3 is bit 3, row 4 is bit 4.
					 * Button is pushed if bit read is 0.
					 */
					button_pushed = !(row_value & (1<<row));
					if(button_pushed) {
						/* Button has been pushed so call handler if one was
						 * provided. In any case, we record this as the last 
						 * button pushed.
						 */
						last_button_pushed = keypad_buttons[row][active_column];
						if(button_handler) {
							(*button_handler)(last_button_pushed);
						}						
						/* Drop out of the for loop */
						break;
					}	
				}
			}
		}	
	}
	
	/* Move on to make the next column active - we'll check it the next time
	 * this function is called.
	 */
	active_column++;
	if(active_column > 4) {
		active_column = 1;
	}
	
	/* Update the port E output value for the new active column.
	 */
	PORTE = portE_column_strobe_values[active_column];
#endif
}

/* Return the last button pushed. If interrupts were on, we turn them off so
 * we don't get interrupted between reading and setting the last_button_pushed
 * variable.
 */
char keypad_button_pushed(void) {
#ifdef AVR
	char c;
	uint8_t interrupts_enabled;
	
	interrupts_enabled = bit_is_set(SREG, SREG_I);
	cli();
	c = last_button_pushed;
	last_button_pushed = 0;
	if(interrupts_enabled) {
		sei();
	}
	return c;
#else
	/* Non AVR - no keypad button is ever pressed. We put
	 * in a delay for 500 microseconds just so we don't 
	 * chew up too much CPU time with busy waiting.
	 */
	usleep(500);
	return 0;
#endif
}
