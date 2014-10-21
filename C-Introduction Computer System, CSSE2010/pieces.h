/*
 * pieces.h
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 */

#ifndef PIECES_H_
#define PIECES_H_

#include <stdint.h>

/* Maximum width/height of any one piece. Change this if you want to 
 * support larger pieces
 */
#define MAX_PIECE_DIMENSION 3

/*
 * Type used to store row data. Must be able to hold BOARD_WIDTH number
 * of bits (defined in board.h)
*/
typedef uint8_t rowtype;

/*
 * Pieces are stored as bit patterns in an array of rows. We record as 
 * many rows as the maximum piece size. 
 * We also record the x and y dimensions of the piece (these will 
 * get swapped when we rotate the piece). We also record the current
 * column of the piece. The column number ranges from the BOARD_WIDTH
 * - 1 to 0 and indicates the bit position (e.g. 6 on the left (if the 
 * BOARD_WIDTH is 7 bits) to 0 on the right)
 *
 * Row 0 is the bottom of the piece - there must always be one bit
 * of this row that is 1 for a valid piece (i.e. the piece always has
 * some part in row 0). This is true after rotation also (we may need
 * to shift the piece down). 
 *
 * For example, the piece bits could look like the following (if
 * the MAX_PIECE_DIMENSION is 2)
 *
 * __*____ (i.e. 0010000)
 * __**___ (i.e. 0011000)
 *
 * When shifted left one position, this will change to
 * _*_____
 * _**____
 * When now rotated (clockwise), this will change to
 * _**____
 * _*_____
 *
 * The left_column position of the piece is maintained through
 * rotation also. This may mean some shifting is
 * necessary when rotating.
 */

typedef struct {
	rowtype rowdata[MAX_PIECE_DIMENSION];
	uint8_t	x_dimension;	/* X size of piece stored in data */
	uint8_t	y_dimension;	/* Y size of piece stored in data */
	uint8_t	right_column;	/* bit number of right column of piece */
} piece_type;	
	

/* 
 * Randomly choose a piece from the piece library
 */
piece_type generate_random_piece(void);

/*
 * Copy the elements of piece_type from one structure to another 
 */
void copy_piece(piece_type* from, piece_type* to);

/*
 * Attempt to rotate the given piece clockwise by 90 degrees.
 * Returns 1 if successful (and modifies the given piece) otherwise
 * returns 0 (and leaves the given piece unchanged).
 */
int8_t rotate_piece(piece_type* piecePtr);

/* 
 * Attempt to move piece one position to the left/right
 * Returns 1 if successful (and modifies the given piece) otherwise
 * returns 0 (and leaves the piece unchanged).
 * Failure happens only when the piece is against the edge
 */
int8_t move_piece_left(piece_type* piecePtr);
int8_t move_piece_right(piece_type* piecePtr);

#endif /* PIECES_H_ */
