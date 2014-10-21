#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

enum ErrorCode {
    SHOW_USAGE = 1, BAD_MAX_STEPS, MISSING_MAP, BAD_MAP_DIM,
    BAD_MAP_CHAR, MAP_WRONG_SIZE, MISSING_LETTERS
};

typedef struct {
    int valid;
    int row, col;
} Point;

typedef struct {
    int nRows, nCols;
    char **grid;
    Point letters[26];
    int letterStart;
} Map;

Map m;

void error(enum ErrorCode e) {
    switch (e) {
        case SHOW_USAGE:
            fprintf(stderr, "Usage: thebox mapfile [maxsteps]\n");
            break;
        case BAD_MAX_STEPS:
            fprintf(stderr, "Bad max steps.\n");
            break;
        case MISSING_MAP:
            fprintf(stderr, "Missing map file.\n");
            break;
        case BAD_MAP_DIM:
            fprintf(stderr, "Bad map dimensions.\n");
            break;
        case BAD_MAP_CHAR:
            fprintf(stderr, "Bad map char.\n");
            break;
        case MAP_WRONG_SIZE:
            fprintf(stderr, "Map file is the wrong size.\n");
            break;
        case MISSING_LETTERS:
            fprintf(stderr, "Missing letters.\n");
            break;
    }

    exit(e);
}

/* check for missing letters */
void check_teleporters() {
    int stillFinding, count, l;

    for (l = 0, count = 0, stillFinding = 0; l < 26; l++) {
        /* letter is on the map */
        if (m.letters[l].valid) {

            if (stillFinding == 0) {
                /* found start of the sequence */
                stillFinding = 1;
                m.letterStart = l;
            } else if (stillFinding == 2) {
                /* skipped letters */
                error(MISSING_LETTERS);
            }

            count += 1;

        } else {
            /* letter is not on the map */
            if (stillFinding == 1) {
                /* found the end of the sequence */
                stillFinding = 2;

                if (count == 1) {
                    /* only have the letter A */
                    error(MISSING_LETTERS);
                }
            }
        }
    }
}

void check_row(int r) {
    int c;
    
    for (c = 0; c < m.nCols; ++c) {
        char p = m.grid[r][c];
        if (!(p == '.' || p == '/' || p == '\\' || p == '=' ||
              p == '@' || (p >= 'A' && p <= 'Z'))) {
            error(BAD_MAP_CHAR);
        }

        if (p >= 'A' && p <= 'Z') {
            int offset = p - 'A';

            if (m.letters[offset].valid) {
                /* duplicated letter */
                error(MISSING_LETTERS);
            }

            m.letters[offset].valid = 1;
            m.letters[offset].row = r;
            m.letters[offset].col = c;
        }
    }
}

void read_map(char *filename)
{
    int r;
    char d, buffer[10];
    FILE *f = fopen(filename, "r");

    if (!f) {
        error(MISSING_MAP);
    }

    if (fgets(buffer, 10, f) == NULL) {
        error(BAD_MAP_DIM);
    }

    if (sscanf(buffer, "%d %d%1[^\n]\n", &m.nRows, &m.nCols, &d) != 2 ||
            m.nRows < 1 || m.nRows > 999 || m.nCols < 1 || m.nCols > 999) {
        error(BAD_MAP_DIM);
    }

    memset(m.letters, 0, sizeof(Point) * 26);

    m.grid = malloc(m.nRows * sizeof(char *));
    for (r = 0; r < m.nRows; ++r) {
        m.grid[r] = calloc(m.nCols + 2, sizeof(char));
        if (fgets(m.grid[r], m.nCols + 2, f) == NULL ||
                m.grid[r][m.nCols] != '\n') {
            error(MAP_WRONG_SIZE);
        }
        
        check_row(r);
    }

    /* check if too much data */
    if (fgetc(f) != EOF) {
        error(MAP_WRONG_SIZE);
    }

    check_teleporters();

    fclose(f);
}


void print_map()
{
    int r;
    for (r = 0; r < m.nRows; ++r) {
        printf("%s", m.grid[r]);
    }
    puts("");
}

void decode_start_pos(Point *pos, Point *dir, char side, int index) {
    switch (side) {
        case 'N':
            dir->row = 1;
            dir->col = 0;

            pos->row = 0;
            pos->col = index - 1;
            break;
        case 'E':
            dir->row = 0;
            dir->col = -1;

            pos->row = index - 1;
            pos->col = m.nCols - 1;
            break;
        case 'S':
            dir->row = -1;
            dir->col = 0;

            pos->row = m.nRows - 1;
            pos->col = index - 1;
            break;
        case 'W':
            dir->row = 0;
            dir->col = 1;

            pos->row = index - 1;
            pos->col = 0;
            break;
    } 
}


void get_start_pos(Point *pos, Point *dir)
{
    char buffer[10];
    char side;
    int index;

    while (1) {
        printf("(side pos)>");

        if (fgets(buffer, 10, stdin) == NULL) {
            exit(0);
        }

        if (sscanf(buffer, "%c%d", &side, &index) == 2 && \
                strchr(buffer, '\n') && strchr(buffer, ' ') == NULL) {

            if (side == 'N' || side == 'S') {
                if (index >= 1 && index <= m.nCols) {
                    break;
                }
            } else if (side == 'E' || side == 'W') {
                if (index >= 1 && index <= m.nRows) {
                    break;
                }
            }
        }

        /* chew up rest of input */
        if (strchr(buffer, '\n') == NULL) {
            while (1) {
                int c = getchar();
                if (c == EOF) {
                    exit(0);
                } else if (c == '\n') {
                    break;
                }
            }
        }
    }

    decode_start_pos(pos, dir, side, index);
}

void increment_map_point(Point *pos) {
    char c = m.grid[pos->row][pos->col];

    if (c == '.') {
        m.grid[pos->row][pos->col] = '1'; 
    } else if (c >= '1' && c <= '8') {
        m.grid[pos->row][pos->col] += 1; 
    }
}

void change_direction(Point *dir, char c) {
    int temp;
    if (c == '/') {
        /* NESW reflector */
        temp = dir->row;
        dir->row = -1*dir->col;
        dir->col = -1*temp;
    } else if (c == '\\') {
        /* NWSE reflector */
        temp = dir->row;
        dir->row = dir->col;
        dir->col = temp;
    } else if (c == '=') {
        /* reflector */
        dir->row *= -1;
        dir->col *= -1;
    }
}


int move_one_step(Point *pos, Point *dir) {
    int i;
    char c = m.grid[pos->row][pos->col];

    /* check if teleporting */
    if (c >= 'A' && c <= 'Z') {
        i = c - 'A';
        if (i + 1 < 26 && m.letters[i + 1].valid) {
            pos->row = m.letters[i + 1].row;
            pos->col = m.letters[i + 1].col;
        } else {
            pos->row = m.letters[m.letterStart].row;
            pos->col = m.letters[m.letterStart].col;
        }

    }

    /* check for jump forward */
    if (c == '@') {
        pos->row += 4*dir->row;
        pos->col += 4*dir->col;
    }

    /* update direction if necessary */
    change_direction(dir, c);
    
    /* move a space */
    pos->row += dir->row;
    pos->col += dir->col;

    /* check still on the board */
    if (pos->row < 0 || pos->row >= m.nRows || 
            pos->col < 0 || pos->col >= m.nCols) {
        return 1;
    }

    increment_map_point(pos);
    print_map();

    return 0;
}

void start_simulation(int maxSteps) {
    int i;
    Point pos, dir;

    print_map();

    get_start_pos(&pos, &dir);

    increment_map_point(&pos);
    
    print_map();

    for (i = 1; i < maxSteps; i++) {
        if (move_one_step(&pos, &dir)) {
            break;
        }
    }
   
    printf("End of simulation.\n");
}

void reset_map() {
    int r, c;
    for (r = 0; r < m.nRows; r++) {
        for (c = 0; c < m.nCols; c++) {
            if (isdigit(m.grid[r][c])) {
                m.grid[r][c] = '.';
            }
        }
    }
}


int main(int argc, char **argv)
{
    char c;
    int maxSteps = 10;

    if (argc < 2 || argc > 3) {
        error(SHOW_USAGE);
    }

    if (argc == 3) {
        if (sscanf(argv[2], "%d%c", &maxSteps, &c) != 1 
                || maxSteps <= 0 || maxSteps >= 1000) {
            error(BAD_MAX_STEPS);
        }
    }

    read_map(argv[1]);

    while (1) {
        start_simulation(maxSteps);
        reset_map();
    }

    return 0;
}