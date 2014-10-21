/*
 * keypad.h
 * 
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * Assumes we have a PMOD-KYPD connected as follows
 * Columns (top row of PMOD-KYPD J1): connected to Cerebot JF (PE6, PE4, PE7, PE5)
 * Rows (bottom row of PMOD-KYPD J1): connected to Cerebot JE (PD6, PB5, PD4, PB6)
 *
 * The behaviour of keypad is as per Digilent documentation. We drive each
 * column low in turn, then check the rows. If any go low, then the
 * corresponding button has been pushed. 
 */ 

#ifndef KEYPAD_H_
#define KEYPAD_H_

typedef void BUTTONHANDLER(char);

/* Initialise the keypad. We drive all columns high to start with. The
 * check_keypad_column() function below will need to be called 
 * regularly. 
 *
 * The argument (handler) is a pointer to a function which accepts a single character. 
 * This function will be called when a button push event happens. No function will
 * be called if a NULL pointer is provided.
 *
 * Note that the handler will be called with the same interrupt status as the
 * check_keypad_column() function, i.e. if check_keypad_column() is called from
 * an interrupt handler whilst interrupts are disabled then the handler will be
 * called with interrupts being disabled. Handlers should be kept brief if 
 * being called with interrupts disabled.
 */
void init_keypad(BUTTONHANDLER* handler);

/* This function should be repeatedly called (e.g. every millisecond or few)
 * to check for buttons pushed on each column of the keypad. (One column is 
 * checked on every call.
 * If a button is found to be pressed that wasn't previously pressed, then the 
 * handler provided by the user is called, with the key name as the argument.
 */
void check_keypad_column(void);

/* Returns the most recent button pushed, or the null character if no button
 * has been pushed since this function was last called. If multiple buttons 
 * have been pushed since this function was called, only the most recent
 * button push will be returned.
 */
char keypad_button_pushed(void);


#endif /* KEYPAD_H_ */
