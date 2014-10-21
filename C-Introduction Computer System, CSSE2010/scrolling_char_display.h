/*
 * scrolling_char_display.h
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#ifndef SCROLLING_CHAR_DISPLAY_H_
#define SCROLLING_CHAR_DISPLAY_H_

#include <stdint.h>

/* Sets the text to be displayed. The message will only
 * be displayed after the current message (if any). (Only
 * one message can be queued for display (i.e. to be 
 * displayed after the current message) - from the last
 * call to this function.)
 */
void set_scrolling_display_text(char* string);

/* Scroll the display. Should be called whenever the display
 * is to be scrolled one pixel to the left. It is assumed that 
 * this happens much less frequently than display_row() is
 * called. (This accesses the "display" value directly.)
 * Returns 1 while a message is still scrolling, 0 when done.
 */
uint8_t scroll_display(void);
	
#endif /* SCROLLING_CHAR_DISPLAY_H_ */
