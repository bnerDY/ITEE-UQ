/*
 * external_interrupt.h
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * We assume we have 4 push buttons connected to pins D0 to D3 (external
 * interrupts 0 to 3). We set up interrupts waiting for a falling edge.
 */ 

#ifndef EXTERNAL_INTERRUPT_H_
#define EXTERNAL_INTERRUPT_H_

#include <stdint.h>

/* Set up external interrupts. We will capture falling edge events.
 * We assume that global interrupts are enabled at some point after
 * this.
 */
void init_external_interrupts(void);

/* Return the last button pushed (0 to 3) or -1 if no button
 * has been pushed since the last call.
 */
int8_t button_pushed(void);

#endif /* EXTERNAL_INTERRUPT_H_ */
