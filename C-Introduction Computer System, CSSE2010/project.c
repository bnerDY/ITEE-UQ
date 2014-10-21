/*
 * project.c - main file 
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#include "board.h"
#include "led_display.h"
#include "keypad.h"
#include "serialio.h"
#include "timer2.h"
#include "score.h"
#include "scrolling_char_display.h"
#include "external_interrupt.h"
#include "terminalio.h"
#include <stdio.h>
#include <stdint.h>

#ifdef AVR
#include <avr/io.h>
#include <avr/interrupt.h>
#else
/* For non AVR build, we remove our interrupt enable */
#define sei(arg)
#endif

/*
 * Function prototypes - these are defined below main()
*/
void initialise_hardware(void);
void splash_screen(void);
void new_game(void);
void play_game(void);
void handle_game_over(void);

/*
 * main -- Main program.
 */
int main(void) {		
	/* Setup all our hardware peripherals and call backs. This 
	 * will turn on interrupts.
	 */
	initialise_hardware();

	/* Show the splash screen message. This returns when 
	 * message display is complete. 
	 */
	splash_screen();
	
	/* Play the game - forever */
	while(1) {
		new_game();
		play_game();
		handle_game_over();
	}
}

void new_game(void) {
	/* 
	 * Initialise the board, and display it. 
	 */
	init_board();
	copy_board_to_led_display();
	
	/* Initialise the score module */
	init_score();
	
	/* Clear any button pushes and serial input. The (void) cast
	 * indicates that we are discarding the return value.
	 */
	(void)button_pushed();
	(void)keypad_button_pushed();
	clear_serial_input_buffer();
}

/* Play the game */
void play_game(void) {
	uint32_t current_time, last_piece_drop_time;
	uint8_t board_updated = 0;		/* 1 if we have to redraw display */
    uint16_t delay;
	char input;
	
	/* Event loop that waits for various times to be reached and whether
	 * various button events have happened.
	 */	
	current_time = get_clock_ticks();
	last_piece_drop_time = current_time;
	while(1) {
		/* We know board_updated is 0 at this point */
		current_time = get_clock_ticks();
        /* Drop a piece every 1000ms. */
        delay = 1000;
        if (get_score()>=10) {
            delay = 400;
        }
        if (get_score()>=40) {
            delay = 300;
        }
        if (get_score()>=60) {
            delay = 200;
        }
        if (get_score()>=80) {
            delay = 100;
        }
		if(current_time >= last_piece_drop_time + delay) {
			
			if(have_current_piece()) {
				/* Attempt to drop piece by 1 row */
				board_updated = attempt_drop_piece_one_row();
				if(!board_updated) {
					/* Couldn't drop piece - add to board */
					fix_piece_to_board();
					board_updated = 1;
				}
				last_piece_drop_time = current_time;
			} else {
				/* No current piece - add one */
				if(add_random_piece()) {
					/* Addition of piece succeeded */
					board_updated = 1;
				} else {
					/* Addition of piece failed - game over */
					return;
				}				
			}
		}
		
		/* Check for button pushes and/or serial input. */
		input = keypad_button_pushed();
		if(!input) {
			input = button_pushed();
			if(input >= 0 && input <= 3) {
				/* Button was pushed (0 to 3) - turn these into lower case
				 * characters, i.e. 0 to 'a', 1 to 'b' etc.
				 */
				input += 97;	/* ASCII code for 'a' */
			} else {
				/* No button was pushed */
				input = 0;
			}
		}
		if(!input && serial_input_available()) {
			/* No keypad or push button was pushed, but serial input is
			 * available. Fetch it. We can use the serial port
			 * to emulate the push buttons etc below.
			 */
			input = fgetc(stdin);
		}
		switch(input) {
			case 'A':	/* Keypad 'A' */
				/* Attempt to rotate the current piece */
				if(have_current_piece()) {
					board_updated |= attempt_rotation();
				}
				break;
			case '1':	/* Keypad '1' */
				/* Attempt to drop the piece by one row */
				if(have_current_piece()) {
					if(!attempt_drop_piece_one_row()) {
						fix_piece_to_board();
					}
					board_updated = 1;
					last_piece_drop_time = current_time;
				}				
				break;
			case 'D':	/* Keypad 'D' */
				/* Attempt a move to the right */
				if(have_current_piece()) {
					board_updated = attempt_move(MOVE_RIGHT);
				}				
				break;
			case '0':	/* Keypad '0' */
				/* Attempt a move to the left */
				if(have_current_piece()) {
					board_updated = attempt_move(MOVE_LEFT);
				}				
				break;
                
			case 'a':/* Button 0 */
                while (1) {
                    ;
                    input = keypad_button_pushed();
                    if(!input) {
                        input = button_pushed();
                        if(input >= 0 && input <= 3) {
                            /* Button was pushed (0 to 3) - turn these into lower case
                             * characters, i.e. 0 to 'a', 1 to 'b' etc.
                             */
                            input += 97;	/* ASCII code for 'a' */
                        } else {
                            /* No button was pushed */
                            input = 0;
                        }
                    }
                    if(!input && serial_input_available()) {
                        /* No keypad or push button was pushed, but serial input is
                         * available. Fetch it. We can use the serial port
                         * to emulate the push buttons etc below.
                         */
                        input = fgetc(stdin);
                    }
                    if (input =='a') {
                        break;
                    }
                    
                }
				/* Pause/unpause the game */
				/* UNIMPLEMENTED FEATURE */
				break;
                
            case 'B':
                if(have_current_piece()) {
                    while(!attempt_drop_piece_one_row()==0) {
                        board_updated = 1;
                        last_piece_drop_time = current_time;
					}
				}
				break;
                
            case 'E':/*Start a new game*/
                if (have_current_piece()) {
                    new_game();
                    init_row();
                    init_score();
                    move_cursor(10,14 );
                    printf("Score: %u",get_score());
                    move_cursor(10,17);
                    printf("Complete row: %u", get_row());
                }
                break;
			default:
				/* Ignore other button pushes or null event. */
				break;				
		}
		
		if(board_updated) {
			/* Update display of board since its appearance has changed. */
			copy_board_to_led_display();
			board_updated = 0;
		}
	}
}

/* Handle the game being over. By default - pause forever. */
void handle_game_over(void) {
	while(1) {
		; /* Do nothing */
	}
}

/* Function to be called on every interrupt of timer 2 (every ms). Note that
 * interrupts are still disabled when this function is called.
 */
void timer_activities(void) {
	/* Update our LED display */
	display_row();
	
	/* Check the next column of our keypad for any button pushes. */
	check_keypad_column();
}

void initialise_hardware(void) {
	/* Initialise hardware modules (interrupts, data direction
	 * registers etc. This should only need to be done once.
	 */
	
	/* Initialise serial IO - baud rate of 19200 and no echo */
	init_serial_stdio(19200, 0);

	/* Initialise the LED board display */
	init_led_display();
	
	/* Initialise the keypad. We don't provide a handler, we'll
	 * rely on asking the keypad module for the last button pushed.
	 */
	init_keypad(NULL);

	/* Initialise the timer which gives us clock ticks
	 * to time things by. We specify that the timer_activities()
	 * function above should be called on every interrupt.
	 */
	init_timer2(timer_activities);
	
	/* Initialise our external button interrupts */
	init_external_interrupts();
	
	/*
	 * Turn on interrupts (needed for timer, serialIO etc. to work)
	 */
	sei();
}

void splash_screen(void) {
	uint32_t currentTime;
	uint32_t displayLastScrolledTime = 0;
	
	/* Display a message on the serial terminal */
	clear_terminal();
	move_cursor(10,12);
	printf("TETRIS - CSSE2010/CSSE7201 Project\n");

	/* This is the text we'll scroll on the LED display. */
	set_scrolling_display_text("42834821");
    
    /*set score to show in terminal*/
    move_cursor(10,14 );
    printf("Score: %u",get_score());
    
    /*set peice preview*/
    move_cursor(10,2 );
    printf("Next piece:");
    /*set to print complete row*/
    move_cursor(10,17);
    printf("Complete row: %u", get_row());
    
	/* We scroll the message until the display is blank or a keypad button
	 * is pushed or a push button is pushed.
	 */
	while(1) {
		currentTime = get_clock_ticks();
		
		if(currentTime >= displayLastScrolledTime + 150) {
			/* Scroll our message every 150ms. Exit the loop
			 * if finished (scroll_display() returns 0).
			*/
			if(scroll_display() == 0) {
				break;
			}
			displayLastScrolledTime = currentTime;
		}
		if(button_pushed() != -1 || keypad_button_pushed() != 0 || 
				serial_input_available()) {
			/* A push button or keypad button or key was pressed - abort 
			 * showing our splash screen.
			 */
			break;
		}
	}		
}


