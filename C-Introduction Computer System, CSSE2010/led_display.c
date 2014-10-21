/*
 * led_display.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * Module that implements our LED display.
 *
 */

#ifdef AVR
#include <avr/io.h>
#else
#include <stdio.h>
#include "terminalio.h"
#endif

#include "led_display.h"

/* Global variable - see comment in header file */
volatile uint16_t display[NUM_ROWS];

void init_led_display(void) {
	uint8_t i;
#ifdef AVR

	/* Set ports A and C to be outputs (except most significant
	 * bit of port C) 
	 */
	DDRA = 0xFF;
	DDRC = 0x7F;

	/* Set 3 least significant bits of G to be outputs */
	DDRG = 0x07;
#endif

	/* Empty the display */
	for(i=0; i<NUM_ROWS; i++) {
		display[i] = 0;
	}
}

void display_row(void) {	
#ifdef AVR
	/* Keep track of the row number we're up to. ("static" 
	 * indicates that the variable value will be remembered 
	 * from one function execution to the next.)
	 */
	static uint8_t row = 0;

	/* Increment our row number (and wrap around if necessary) */
	if(++row == NUM_ROWS) {
		row = 0;
	}

	/* Output our row number to port G. This assumes the other 
	 * bits of port G are not being used. If they are, then
	 * this line of code needs to be changed.
	 */
	PORTG = row;

	/* Output the correct row data to ports A and C. (Port C gets
	 * the high byte, port A gets the low byte.) We need to invert
	 * the data since we need a low output for the LED to be lit. 
	 * Note - most significant bit is not displayed/used.
	 */
	PORTA = ~(uint8_t)(display[row] & 0xFF);
	PORTC = ~(uint8_t)((display[row] >> 8)& 0x7F);
#endif
}

#ifndef AVR
/* Non  AVR - we output the board using terminal IO */
void display_board(void) {
	int row, col;
	int startX = 50;
	int startY = 4;
	draw_horizontal_line(startY - 2, startX - 2, startX + 8);
	draw_horizontal_line(startY + 16, startX - 2, startX + 8);
	draw_vertical_line(startX - 2, startY - 2, startY + 16);
	draw_vertical_line(startX + 8, startY - 2, startY + 16);
	for(col = 14; col >= 0; col--) {		
	    move_cursor(startX, startY + 14 - col);
	    for(row = 0; row <= 6; row++) {
			if((display[row] >> col) & 1) {
				reverse_video();
				printf(" ");
				normal_display_mode();
			} else {
				printf(" ");
			}
		}				
	}
}
					 
#endif
