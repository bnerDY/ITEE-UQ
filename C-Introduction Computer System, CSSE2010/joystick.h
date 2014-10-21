/* 
 * joystick.h
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#ifndef JOYSTICK_H_
#define JOYSTICK_H_

#ifdef AVR

/* Global variables that can be checked to find out the status
 * of the joystick. JoystickX is a number between 0 and 1023 inclusive.
 * JoystickY is a number between 0 and 1023 inclusive. 
 * JoystickButtons: 
 *		bit 0 - joystick itself
 *		bit 1 - button 1
 *		bit 2 - button 2
 * Bits are set (1) if button is pressed.
 */
extern uint16_t JoystickX;
extern uint16_t JoystickY;
extern uint8_t JoystickButtons;

/* Set up the joystick interface ready for use. This must be called
 * before any communication with the joystick. The joystick is assumed 
 * to be connected to the SPI port (i.e. lower 4 bits on AVR port B).
 */
void init_joystick(void);

/*
 * Check the joystick and update the global variables above, i.e.
 * JoystickX, JoystickY, JoystickButtons. We also turn on/off the 
 * LEDs on the Joystick. If led1 is true, LD1 will be on otherwise
 * off. If led2 is true, LD2 will be on, otherwise off.
 * Calling this function will take at least 55 microseconds to
 * complete the communication.
 */
void check_joystick(uint8_t led1, uint8_t led2);

#endif	/* AVR */
#endif	/* JOYSTICK_H_ */
