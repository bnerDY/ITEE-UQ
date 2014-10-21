/*
 * pieces.c
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#include "pieces.h"
#include "board.h"
#include <stdlib.h>
/* Stdlib needed for rand() - random number generator */


/*
 * Define the piece library. Rows are defined FROM THE BOTTOM UP.
 * The hexadecimal numbers below can be expressed diagrammatically
 * as below (* represents 1, _ represents 0). There are 5 pieces
 * defined for this version of the program.
 * NOTE. Pieces must not exceed MAX_PIECE_DIMENSION in either dimension.
 * NOTE: Pieces must be right aligned and bottom aligned 
 * NOTE: piece_type is defined in pieces.h
*/
#define NUM_PIECES_IN_LIBRARY 6
piece_type piece_library[NUM_PIECES_IN_LIBRARY] = {
	{ {0x01, 0x00, 0x00}, 1, 1, 0},
	{ {0x01, 0x01, 0x01}, 1, 3, 0},
	{ {0x03, 0x03, 0x00}, 2, 2, 0},
	{ {0x07, 0x04, 0x00}, 3, 2, 0},
	{ {0x07, 0x02, 0x00}, 3, 2, 0},
    { {0x03, 0x06, 0x00}, 3, 2, 0}}; /*add new stick*/
	
/* GRAPHICAL REPRESENTATION
 * ____ = 0000 = 0x00
 * ____ = 0000 = 0x00
 * ___* = 0001 = 0x01
 *
 * ___* = 0001 = 0x01
 * ___* = 0001 = 0x01
 * ___* = 0001 = 0x01
 *
 * ____ = 0000 = 0x00
 * __** = 0011 = 0x03
 * __** = 0011 = 0x03
 * 
 * ____ = 0000 = 0x00
 * _*__ = 0100 = 0x04
 * _*** = 0111 = 0x07
 *
 * ____ = 0000 = 0x00
 * __*_ = 0010 = 0x02
 * _*** = 0111 = 0x07
 *
 * ____ = 0000 = 0x00
 * _**_ = 0110 = 0x06
 * __** = 0011 = 0x03
 */
	
	
piece_type generate_random_piece(void) {
	piece_type piece;
	int8_t i;
	int piece_num = rand() % NUM_PIECES_IN_LIBRARY;
	piece = piece_library[piece_num];
	
	/* 
	 * Rotate the piece 0, 90, 180 or 270 degrees - i.e. perform
	 * 0 to 3 rotations  - generate a random number between 0
	 * and 3 inclusive
	 */
	int num_rotations = rand() % 4;
	for(i=0; i < num_rotations; i++) {
		(void)rotate_piece(&piece);
		/* We ignore the return value - doesn't matter
		 * whether successful or not. (Will be unsuccessful
		 * for those pieces taller than they are wide.)
		 */
	}
	
	/* Shift the piece a random number of bits to the left 
	 * Update necessary rows in piece.rowdata[] and 
	 * piece.right_column
	 */
	
	/* YOUR CODE HERE */ 

	return piece;
}

void copy_piece(piece_type* from, piece_type* to) {
	int8_t row;
	for(row=0; row < MAX_PIECE_DIMENSION; row++) {
		to->rowdata[row] = from->rowdata[row];
	}
	to->x_dimension = from->x_dimension;
	to->y_dimension = from->y_dimension;
	to->right_column = from->right_column;
}

/*
 * Attempt to rotate the given piece clockwise by 90 degrees.
 * Returns 1 if successful (and modifies the given piece) otherwise
 * returns 0 (and leaves the given piece unchanged).
 * This method is only unsuccessful if the piece is too close to the
 * left hand side to be rotated.
 */
int8_t rotate_piece(piece_type* piecePtr) {
	/*
	 * We calculate what the rotated piece would look like, 
	 */
	piece_type rotated;
	int8_t row, col, new_row, new_col;
	unsigned char bit_to_copy;
	
	/*
	 * First check if rotation would cause the piece to be off 
	 * the board. (This only happens if the piece is taller than
	 * it is wide and is too close to the left hand side.)
	 */
	if(piecePtr->right_column + piecePtr->y_dimension > BOARD_WIDTH) {
		return 0;
	}
	
	/*
	 * Initialise the rotated piece. (Swap X and Y dimensions.)
	 */
	rotated.x_dimension = piecePtr->y_dimension;
	rotated.y_dimension = piecePtr->x_dimension;
	rotated.right_column = piecePtr->right_column;
	for(row=0; row < MAX_PIECE_DIMENSION; row++) {
		rotated.rowdata[row] = 0;
	}
	
	/*
	 * Iterate over the rows of the existing piece - these
	 * become the columns of the rotated piece. col is a relative
	 * column position (from the rightmost bit of the piece).
	 */
	for(row=0; row < piecePtr->y_dimension; row++) {
		for(col=0; col < piecePtr->x_dimension; col++) {
			new_row = col;
			new_col = piecePtr->y_dimension - 1 - row;
			bit_to_copy = (piecePtr->rowdata[row] >> 
				(piecePtr->right_column + col)) & 0x01;
			rotated.rowdata[new_row] |= 
				bit_to_copy << (piecePtr->right_column + new_col);
		}
	}

	/*
	 * Copy rotated data to original piece
	 */
	copy_piece(&rotated, piecePtr);
	
	/* Indicate success */
	return 1;
}

int8_t move_piece_left(piece_type* piecePtr) {
	int8_t row;
	/*
	 * Check if piece is all the way to the left If so, return.
	 * (Remember, column number is based on bit position - higher
	 * numbers to the left.
	 */
	if(piecePtr->right_column + piecePtr->x_dimension >= BOARD_WIDTH) {
		return 0;
	}
		
	/*
	 * Make the move.
	 */
	for(row=0; row < piecePtr->y_dimension; row++) {
		piecePtr->rowdata[row] <<= 1;
	}
	piecePtr->right_column++;
	return 1;
}

int8_t move_piece_right(piece_type* piecePtr) {
    int8_t row;
	/*
	 * Check if piece is all the way to the left If so, return.
	 * (Remember, column number is based on bit position - lower
	 * numbers to  left.
	 */
	if(piecePtr->right_column <=0) {
		return 0;
	}
    
	/*
	 * Make the move.
	 */
	for(row=0; row < piecePtr->y_dimension; row++) {
		piecePtr->rowdata[row] >>= 1;
	}
	piecePtr->right_column--;
	return 1;
}
	/*
	 * You may wish to model it on move_piece_left above
	 * Your function must return 0 if it's unable to move (e.g.
	 * piece is against the right hand side), 1 otherwise.
	 */
	
	/*
	 * Initially, this function does nothing so we return 0
	 */
