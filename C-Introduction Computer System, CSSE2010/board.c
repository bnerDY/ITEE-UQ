/*
 * board.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * Board data is stored in an array of rowtype (which is wide enough
 * to hold a bit for each column). The bits of the rowtype
 * represent whether that square is occupied or not (a 1 indicates
 * occupied). The least significant BOARD_WIDTH bits are used. The
 * least significant bit is on the right.
 */

#include "board.h"
#include "pieces.h"
#include "score.h"
#include "led_display.h"

/*
 * Function prototypes.
 * Board.h has the prototypes for functions in this module which
 * are available externally, and because we include "board.h" above
 * we do not need to repeat those prototypes.
 */
int8_t piece_overlap(piece_type* piece, int8_t row_num);
void check_for_completed_rows(void);

/*
 * Global variables
 *
 */
rowtype    board[BOARD_ROWS];
piece_type current_piece;/* Current dropping piece */
piece_type next_piece;
int8_t     piece_row_num;	/* Current row number of the bottom of
							 * the current piece, -1 if have 
							 * no current piece  */

/* 
 * Initialise board - no pieces (i.e. set the row data to contain
 * all zeroes.)
 */
void init_board(void) {
	uint8_t i;

	for(i=0; i < BOARD_ROWS; i++) {
		board[i] = 0;
	}
    next_piece = generate_random_piece();
	
	/* -1 in piece_row_num indicates no current piece */
	piece_row_num = -1;
}

/* 
 * Copy board to LED display. Note that difference in definitions of
 * rows and columns for the board and the LED display. The Tetris board
 * has 15 rows (numbered from the bottom), each 7 bits wide (with the 
 * 7 columns numbered as per the bits - i.e. least significant (0) on 
 * the right). The LED display has 7 rows (0 at the top, 6 at the bottom) 
 * with 15 columns (numbered from 0 at the bottom to 14 at the top).
 */
void copy_board_to_led_display(void) {
	/* The board has BOARD_ROWS (e.g. 15), each of width BOARD_WIDTH.
	 * Board row 0 corresponds to LED display column bit 0 etc.
	 * The function updates our LED display to reflect the 
	 * current state of the board.
	 */
	int8_t board_row_num;
	int8_t board_col_num;
	uint16_t led_display_row;
	for(board_col_num = 0; board_col_num < BOARD_WIDTH; board_col_num++) {
		led_display_row = 0;
		for(board_row_num = BOARD_ROWS-1; board_row_num >= 0; board_row_num--) {
			led_display_row <<=1;
			led_display_row |= (board[board_row_num]>>board_col_num)&1;
			/* If the current piece covers this row - add it in also. */
			if(piece_row_num >= 0 && board_row_num >= piece_row_num &&
					board_row_num < (piece_row_num + current_piece.y_dimension)) {
				led_display_row |= 
						((current_piece.rowdata[board_row_num - piece_row_num]
							>>board_col_num)&1);
			}
		}

		/* Copy this row to the LED display. Lower LED display 
		 * row numbers correspond to higher board column numbers
		 */
		display[6-board_col_num] = led_display_row;
	}
#ifndef AVR
	display_board();
#endif
}
			
/*
 * Checks whether have current piece
 */
int8_t have_current_piece(void) {
	return (piece_row_num != -1);
}



/*
 * Add random piece, return false (0) if we can't add the piece - this
 * means the game is over.
 */
int8_t add_random_piece(void) {
    int row, col;
	int startX = 10;
	int startY = 4;
	current_piece = next_piece;
    next_piece = generate_random_piece();
    piece_type *ptr;
    move_cursor(10, 4);
    printf("       ");
    move_cursor(10, 5);
    printf("       ");
    move_cursor(10, 6);
    printf("       ");
    ptr = &next_piece;
    for(col = 4; col >= 0; col--) {
	    for(row = 0; row <= 2; row++) {
			if((ptr->rowdata[row] >> col) & 1) {
				move_cursor(startX + row, startY + 2 - col);
                reverse_video();
				printf(" ");
				normal_display_mode();
			} else {
				printf(" ");
			}
		}
	}
    
	/* We add the piece at a position that ensures it will fit on
	 * the board, even if rotated (i.e. we check it's maximum
	 * dimension and come down that many rows).
	 * This allows rotation without worrying
	 * about whether the piece will end up off the top of the 
	 * board or not.
	 */
	if(current_piece.x_dimension > current_piece.y_dimension) {
		piece_row_num = BOARD_ROWS - current_piece.x_dimension;
	} else {
		piece_row_num = BOARD_ROWS - current_piece.y_dimension;
	}
	if(piece_overlap(&current_piece, piece_row_num)) {
		/* Game is over */
		piece_row_num = -1; /* no current piece */
		return 0;
	} else {
		return 1;
        
	}
}

/*
 * Attempt to move the current piece to the left or right. 
 * This succeeds if
 * (1) the piece isn't all the way to the side, and
 * (2) the board contains no pieces in that position.
 * Returns 1 if move successful, 0 otherwise.
 */
int8_t attempt_move(int8_t direction) {	
	piece_type backup_piece;
	
	/*
	 * Make a copy of our piece in its current position (in case
	 * we need to restore it)
	 */
	copy_piece(&current_piece, &backup_piece);
	
	/*
	 * Move the piece template left/right, if possible (will only 
	 * fail if the piece is up against the side).
	 */
	if(direction == MOVE_LEFT) {
		if(!move_piece_left(&current_piece)) {
			return 0;
		}
	} else {
		if(!move_piece_right(&current_piece)) {
			return 0;
		}
	}
		
	/* 
	 * If we get here, piece is not at edge.
	 * Check that the board will allow a move (i.e. the pieces
	 * won't overlap).
	 */
	if(piece_overlap(&current_piece, piece_row_num)) {
		/*
		 * Current board position does not allow move.
		 * Restore original piece
		 */
		copy_piece(&backup_piece, &current_piece);
		return 0;
	}
	
	/* Move has been made - return success */
	return 1;
}


/*
 * Attempt to drop the piece by one row. This succeeds unless there
 * are squares blocked on the row below or we're at the bottom of
 * the board. Returns 1 if drop succeeded, 
 * 0 otherwise. (If the drop fails, the caller should add the piece
 * to the board.)
*/
int8_t attempt_drop_piece_one_row(void) {
	/*
	 * Check if the piece has
	 * reached the bottom of the board. Nothing to do in this
	 * case - return false - we can't drop it any further.
	 */
	if(piece_row_num == 0) {
        add_to_score(1);
		return 0;
	}
	
	/*
	 * Check whether the piece would intersect with any board
	 * pieces if it dropped one position 
	 */
	if(piece_overlap(&current_piece, piece_row_num - 1)) {
        add_to_score(1);
		return 0;
	}
	
	/*
	 * Only get here if piece drop would succeed - make it happen
	 */
	piece_row_num--;
	return 1;
}


/*
 * Attempt to rotate the piece clockwise 90 degrees. Returns 1 if the
 * rotation is successful, 0 otherwise (e.g. a piece on the board
 * blocks the rotation).
 */
int8_t attempt_rotation(void) {
	/* We calculate what the rotated piece would look like, 
	 * then compute if it would interect with any board pieces
	 */
	piece_type backup_piece;
	
	/*
	 * Make a copy of our piece in its current orientation (in case
	 * we need to restore it)
	 */
	copy_piece(&current_piece, &backup_piece);

	/*
	 * Attempt rotation (will only fail if too close to right hand
	 * side)
	 */
	if(!rotate_piece(&current_piece)) {
		return 0;
	}
	
	/* 
	 * Need to check if rotated piece will intersect with existing
	 * pieces. If yes, restore old piece and return failure
	 */
	if(piece_overlap(&current_piece, piece_row_num)) {
		/*
		 * Current board position does not allow move.
		 * Restore original piece
		 */
		copy_piece(&backup_piece, &current_piece);
		return 0;
	}
	
	/* Move has been made - return success */
	return 1;
}


/*
 * Add piece to board at its current position. We do this using a
 * bitwise OR for each row that contains the piece.
 */
void fix_piece_to_board(void) {
	int8_t i;
	for(i=0; i < current_piece.y_dimension; i++) {
		board[piece_row_num + i] |= current_piece.rowdata[i];
	}
	/*
	 * Indicate that we no longer have a current piece
	 */
	piece_row_num = -1;
	check_for_completed_rows();
}

void check_for_completed_rows(void) {
    int8_t i;
    for (i=0; i <BOARD_ROWS-1; i++ ) {
        if (board[i] ==((1 << BOARD_WIDTH) - 1)) {
            add_to_score(4); /*add score to complete one row*/
            add_to_row(1);  /*add row count for complete one row*/
            for (i; i<BOARD_ROWS-1; i++) {
                board[i] = board [i+1];
            }
            check_for_completed_rows();
        }
        
    }
    board[BOARD_ROWS-1]=0;
    move_cursor(10,14 );
    printf("Score: %u",get_score());
    move_cursor(10,17);
    printf("Complete row: %u", get_row());
	
	/* Suggested approach is to iterate over all the rows (0 to
	 * BOARD_ROWS -1)in the board and check if the row is all ones
	 * i.e. matches ((1 << BOARD_WIDTH) - 1).
	 * If a row of all ones is found, the rows above the current
	 * one should all be moved down one position and a zero row 
	 * inserted at the top. 
	 * Repeat this process if more than one completed row is
	 * found.
	 *
	 * e.g. if rows 2 and 4 are completed (all ones), then
	 * rows 0 and 1 at the bottom will remain unchanged
	 * old row 3 becomes row 2
	 * old row 5 becomes row 3
	 * old row 6 becomes row 4 
	 * ...
	 * old row BOARD_ROWS - 1 becomes row BOARD_ROWS - 3;
	 * row BOARD_ROWS - 2 (second top row) is set to 0
	 * row BOARD_ROWS - 1 (top row) is set to 0
	 */
	
}

/* 
 * Check whether the given piece will intersect with pieces already on the
 * board (assuming the piece is placed at the given row number).
*/
int8_t piece_overlap(piece_type* piece, int8_t row_num) {
	int8_t row;
	for(row=0; row < piece->y_dimension; row++) {
		if(piece->rowdata[row] & board[row_num + row]) {
			/* Got an intersection (AND is non-zero) */
			return 1;
		}
	}
	return 0;
}

