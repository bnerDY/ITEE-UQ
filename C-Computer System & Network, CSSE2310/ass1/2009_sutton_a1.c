/* COMP2303 Assignment 1
**
** Sample solution (without advanced functionality) by Peter Sutton
** March/April 2009
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

/* DIGITS lists all of the valid characeters on first line of a grid file */
/* LETTERS lists all of the valid characeters on other lines of a grid file */
#define DIGITS "0123456789"
#define LETTERS "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

/* Maximum grid dimension (basic functionality) */
#define MAXDIMENSION 20

#define ERROR_ARGS 1
#define ERROR_FILE 3
#define ERROR_GRIDSIZE 4
#define ERROR_GRIDLINES 5

/* Name of output file - defaults to "found" */
char* outputFileName = "found";
/* Names of grid file and list file */
char* gridFileName;
char* listFileName;
/* Boolean - determines whether we sort or not. By default, we won't. */
int sort = 0;
/* The grid of letters that we search within */
char grid[MAXDIMENSION][MAXDIMENSION];
/* Size of the grid */
int gridDimension;
/* Array that indicates whether a grid letter is used or not */
int letterUsed[MAXDIMENSION][MAXDIMENSION];


/* Function prototypes - see descriptions below */
void process_command_line(int argc, char* argv[]);
void error(int errCode, char* message);
FILE* open_file(char* name, char* mode);
void read_grid(FILE* gridFileHandle);
char* read_line(FILE*, int* lineLengthPtr);
int search(char* word, int wordLength);
int search_from_starting_point(char* word, int wordLength, int startX,
	int startY, int deltaX, int deltaY);
void process_list(FILE* listFile, FILE* outputFile);


int main(int argc, char* argv[]) {
    FILE* outputFile;
    FILE* gridFile;
    FILE* listFile;

    process_command_line(argc, argv);
    /*
    printf("%d %s %s %s\n", sort, outputFileName, gridFileName, listFileName);
    */
    
    /* Make sure we can open all the files - exit if we don't. */
    /* Note that exiting will close any open files, we do not have to close
    ** them explicitly. */
    gridFile = open_file(gridFileName, "r");
    listFile = open_file(listFileName, "r");
    outputFile = open_file(outputFileName, "w");

    read_grid(gridFile);
    process_list(listFile, outputFile);

    return 0;
}

/*****************************************************************************
** process_list
**
** Read words one by one from the given list file and see if they can be 
** found in the grid. Print them out to the output file if they are found
*/
void process_list(FILE* listFile, FILE* outputFile) {
    char* word;
    int wordLength;
    int x, y;

    while((word = read_line(listFile, &wordLength))) {
	/* Have word read from list file */
	if(search(word, wordLength)) {
	    /* Word matched - print it out to our output file */
	    fprintf(outputFile, "%s\n", word);
	}
    }
    /* Now print all unused letters */
    for(x = 0; x < gridDimension; x++) {
	for(y = 0; y < gridDimension; y++) {
	    if(!letterUsed[x][y]) {
		printf("%c", grid[x][y]);
	    }
	}
    }
    /* Terminating newline on our list of unused letters */
    printf("\n");
}

/*****************************************************************************
** search
**
** Search for the given word in the grid. If found, we indicate that those
** letters are used in the "letterUsed" array and return true. Otherwise, 
** we return false.
*/
int search(char* word, int wordLength) {
    int deltaX, deltaY, startX, startY;
    int minStartingX, maxStartingX, minStartingY, maxStartingY;

    /* If the word is bigger than the grid size, abort - we won't find it */
    if(wordLength > gridDimension) {
	return 0;
    }
    /* Search the grid for the given word. We have 8 different directions
    ** to search - all combinations of deltaX,deltaY except 0,0. */
    for(deltaX = -1; deltaX <= 1; deltaX++) {
	for(deltaY = -1; deltaY <= 1; deltaY++) {
	    if(deltaX == 0 && deltaY == 0) {
		/* This isn't a valid search direction */
		continue;
	    }
	    /* We now have our search direction - need to iterate over
	    ** all possible starting points in the grid - we only choose 
	    ** those starting points from which the word will actually fit. 
	    ** (Note that Y increases from top to bottom.) */
	    minStartingX = 0;
	    maxStartingX = gridDimension - 1;
	    minStartingY = 0;
	    maxStartingY = gridDimension - 1;
	    if(deltaX < 0) { /* Looking to the left */
		minStartingX = wordLength - 1;
	    } 
	    if(deltaX > 0) { /* Looking to the right */
		maxStartingX -= (wordLength - 1);
	    }
	    if(deltaY < 0) { /* Looking up */
		minStartingY = wordLength - 1;
	    }
	    if(deltaY > 0) { /* Looking down */
		maxStartingY -= (wordLength - 1);
	    }
	    for(startX = minStartingX; startX <= maxStartingX; startX++) {
		for(startY = minStartingY; startY <= maxStartingY; startY++) {
		    if(search_from_starting_point(word, wordLength, 
			    startX, startY, deltaX, deltaY)) {
			/* Found match */
			return 1;
		    } /* else try the next starting point */
		}
	    }
	}
    }
    /* No match found */
    return 0;
}

/*****************************************************************************
** search_from_starting_point
**
** Search in the grid for the given word from the given starting point and
** in the given direction. If we find the word, we indicate this in the 
** letterUsed array and return true, otherwise, we return false.
*/
int search_from_starting_point(char* word, int wordLength, int startX,
	int startY, int deltaX, int deltaY) {
    int charNum, x, y;

    /* Starting point */
    x = startX;
    y = startY;

    for(charNum = 0; charNum < wordLength; charNum++) {
	if(word[charNum] != grid[x][y]) {
	    /* No match at this starting point, in this direction */
	    return 0;
	} 
	x += deltaX;
	y += deltaY;
    }
    /* If we get here, every character in the word matched.
    ** Update our usage array - and return true */
    x = startX;
    y = startY;
    for(charNum = 0; charNum < wordLength; charNum++) {
	letterUsed[x][y] = 1;
	x += deltaX;
	y += deltaY;
    }
    return 1;
}

/*****************************************************************************
** error
**
** Prints an error message and exits with the given exit status.
*/
void error (int code, char* argument) {
    switch(code) {
	case 1:
	    fprintf(stderr, "Usage: wordsearch [-sort] [-out output-filename] "
		    "grid-filename list-filename\n");
	    break;
	case 3:
	    fprintf(stderr, "Error: could not open file %s\n", argument);
	    break;
	case 4:
	    fprintf(stderr, "Error: invalid grid size\n");
	    break;
	case 5:
	    fprintf(stderr, "Error: invalid grid lines\n");
	    break;
    }
    exit(code);
}

/*****************************************************************************
** read_grid
**
** Read the contents of the given filehandle into the grid.
*/
void read_grid(FILE* gridFileHandle) {
    int numDigits, numLetters;
    int i, j;
    char* line;
    int lineLength;

    /* Read the first line of the file.  */
    if(!(line = read_line(gridFileHandle, &lineLength))) {
	error(ERROR_GRIDSIZE, "");
    }

    /* Have read a line - make sure it contains only digits - the number
    ** of digits should match the line length. (strspn() will return the 
    ** number of characters in set DIGITS before any other characters in the
    ** line. The number of digits should be > 0.
    */
    numDigits = strspn(line, DIGITS);
    if(numDigits != lineLength || numDigits < 1) {
	error(ERROR_GRIDSIZE, "");
	/* Never reach here - error() function will exit. */
    }

    /* If we get here, the first line consists only of digits. Get the
    ** dimension and check it is valid. (In this case we assume 
    ** dimensions > MAXDIMENSION are invalid, since our 
    ** program can't handle them.)
    */
    gridDimension = atoi(line);
    if(gridDimension < 1 || gridDimension > MAXDIMENSION) {
	error(ERROR_GRIDSIZE, "");
    }

    /* We have a valid grid dimension - read the lines in. */
    for(i = 0; i < gridDimension; i++) {
	if(!(line = read_line(gridFileHandle, &lineLength))) {
	    /* Ran out of lines to read before we expected to */
	    error(ERROR_GRIDLINES, "");
	}
	/* Have a line - check it only contains letters, and is the right
	** length. */
	numLetters = strspn(line, LETTERS);
	if(numLetters != gridDimension || lineLength != gridDimension) {
	    error(ERROR_GRIDLINES, "");
	}
	/* Line is valid - copy it to our grid. We also
	** initialise our record of whether the letter is used or not.
	*/
	for(j=0; j < gridDimension; j++) {
	    grid[i][j] = line[j];
	    letterUsed[i][j] = 0;
	}
    }
    /* Should now have no lines left */
    if(fgetc(gridFileHandle) != EOF) {
	error(ERROR_GRIDLINES, "");
    }
    
    /* We close the file when we're done */
    fclose(gridFileHandle);

#if 0
    /* Print the grid out for testing */
    for(i = 0; i < gridDimension; i++) {
	for(j = 0; j < gridDimension; j++) {
	    printf("%c", grid[i][j]);
	}
	printf("\n");
    }
#endif
}

/*****************************************************************************
** open_file
**
** Open the file with the given name and mode and return the file handle.
** If the file can not be opened, print an error message and exit.
*/
FILE* open_file(char* filename, char* mode) {
    FILE* fileHandle;
    fileHandle = fopen(filename, mode);
    if(!fileHandle) {
	error(ERROR_FILE, filename);
    }
    return fileHandle;
}

/*****************************************************************************
** process_command_line
**
** Checks the arguments are as expected and sets global variables 
** appropriately. If arguments are not as expected, a usage error is 
** produced and the program exits.
*/
void process_command_line(int argc, char* argv[]) {
    /* Check arguments - We will advance argv and decrement argc as we 
    ** process the options. */
    int foundOut = 0;
    
    /* Don't consider the program name */
    argv++;
    argc--;

    /* Process arguments until we don't have any options left */
    while(argc > 0) {
	/* Expecting an option - see what it is */
	if(strcmp(argv[0], "-sort") == 0 && sort != 1) {
	    /* -sort argument given on the command line, and we haven't seen 
	    ** it before. */
	    sort = 1;
	    argv++;
	    argc--;
	} else if(strcmp(argv[0], "-out") == 0 && argc >= 1 && foundOut != 1) {
	    /* -out argument given on the command line - with the correct
	    ** number of remaining arguments */
	    foundOut = 1;
	    outputFileName = argv[1];
	    argv += 2;
	    argc -= 2;
	} else {
	    /* Have come across an argument we don't recognise - assume
	    ** end of options 
	    */
	    break;
	}
    }
    /* There should be exactly two arguments remaining */
    if(argc != 2) {
	error(1, "");
	/* Doesn't return - this point never reached */
    }
    /* There were 2 arguments left - these are the grid and list filenames */
    gridFileName = argv[0];
    listFileName = argv[1];
}

/*****************************************************************************
** read_line
**
** Reads a line (terminated by \n) from the given file handle.
** If the read fails, or the line is not terminated by \n, then NULL is
** returned, otherwise, a pointer to a static buffer containing the line 
** is returned - this pointer may be used until the
** next call to this function. (The newline is stripped from the line
** before it is returned and all letters are converted to upper case.) 
** The number of characters in the line (before the newline) is stored in 
** the variable pointed to by lengthPtr.
**
** We use MAXDIMENSION + 2 below so that we have enough space in the buffer
** for the grid characters, a newline and a terminating null character.
*/
char* read_line(FILE* fileHandle, int* lengthPtr) {
    static char buffer[MAXDIMENSION + 2];
    int i, lineLength;

    if(!fgets(buffer, MAXDIMENSION + 2, fileHandle)) {
	/* Read failed - maybe EOF */
	return NULL;
    }
    lineLength = strlen(buffer);
    if(buffer[lineLength - 1] != '\n') {
	/* Line was not terminated by a newline */
	return NULL;
    }
    /* Replace the newline with a terminating null */
    buffer[--lineLength] = '\0';
    
    /* Convert each lowercase letter to uppercase. */
    for(i = 0; i < lineLength; i++) {
	buffer[i] = (char)toupper((int)buffer[i]);
    }

    *lengthPtr = lineLength;
    return buffer;
}

