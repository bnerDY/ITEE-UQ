/*
 * board.h
 *
 * Original version by Peter Sutton. Modified by <Martin Yu>
 *
 * Function prototypes for those functions available externally
 */

#include <stdint.h>

/*
 * The board is 15 rows in size. Row 0 is considered to be at the bottom, 
 * row 14 is at the top. Each row is 7 columns wide.
 */
#define BOARD_ROWS 15
#define BOARD_WIDTH 7

#define MOVE_LEFT 0
#define MOVE_RIGHT 1

/*
 * Initialise the board.
 */
void init_board(void); 

/* 
 * Copy board to LED display 
 */
void copy_board_to_led_display(void);

/*
 * Check if have current piece or not
 */
int8_t have_current_piece(void);

/*
 * Add a random piece to the top of the board. Returns 1 on success, 
 * 0 on failure. This can only fail when the game is over - i.e. the
 * piece added would intersect with the pieces already on the board.
 */
int8_t add_random_piece(void);

/*
 * attempt_move
 * Attempts a move of the current piece in the given direction 
 * (direction should be MOVE_LEFT or MOVE_RIGHT).
 * Returns 0 on failure (e.g. piece was against edge or other pieces on 
 * the board prevented the move). Returns 1 on success. 
 * Should only be called if we have a current piece.
 */
int8_t attempt_move(int8_t direction);


/*
 * Attempt to drop the current piece by one row. Returns 0 on failure,
 * 1 on success. Should only be called if we have a current piece.
 */
int8_t attempt_drop_piece_one_row(void);

/*
 * Attempt rotation (clockwise) of the current piece on the board. 
 * Returns 0 on failure, 1 on success. Should only be called if
 * we have a current piece.
 */
int8_t attempt_rotation(void);

/*
 * Add the current piece to board. (This should be called when the 
 * current piece can drop no further.) Should only be called if we
 * have a current piece.
 */
void fix_piece_to_board(void);
