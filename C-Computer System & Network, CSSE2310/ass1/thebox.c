#include<stdio.h>
#include<stdlib.h>

/* Basic definition for errors.*/
#define ERROR_ARGS 1
#define ERROR_STEP 2
#define ERROR_FILE 3
#define ERROR_DIME 4
#define ERROR_CHAR 5
#define ERROR_SIZE 6
#define ERROR_MISS 7

/* Basic definition for direction.*/
#define N 0
#define S 1
#define W 2
#define E 3

typedef struct Box	
{
	int x;
	int y;
	int direction;
}BOX;

/*Global variables.*/
int row,col;		
char **map;			
FILE *fp;			
BOX box;
BOX tempBox;
int first = 0;
int maxteps;
char *mapFileName;
int count = 0;

/* The function protypes.*/
int GetDir(int dir, char ch);
void Move(int dir, int x, int y);
void DisplayMap();						
void initBox(char dir, int pos);
void initMap(char* name, char* mode);							
char** alloc_map(int row, int col);		
int FindNext(int *x, int *y, char ch, int dir);
int IsLegal(int x, int y);
int process_command_line (int argc, char* argv[]);
void error (int errCode, char* message);
void GetLast(int x, int y, int dir);
void GetNextNext(char ch);
void CheckMap();

int main(int argc, char *argv[])
{
	char str[10];
	char dir;
	int num;
    
	box.x = box.y = 0;
	maxteps = process_command_line(argc, argv);
    while(1){
        count = 0;
        initMap(mapFileName, "r");
        while(1){
            printf("%s", "(side pos)>");
            if(fgets(str, 10, stdin) == NULL){
                exit(0);
            }
            if (str[0] == '\n'){
                continue;
            }
            sscanf(str,"%c%d", &dir, &num);
            if(dir == 'E' || dir == 'W' || dir == 'S' || dir == 'N'){
                break;
            }
            else{
                continue;
            }
        }
        initBox(dir, num);
    }
	return 0;
}

/****************************************************************************
** Get the direction after the box meet any operation.
** Only /, \, and = would change the direction
*/

int GetDir(int dir, char ch){
	/*	DisplayMap(); For test*/
	switch(ch){
        case '/':
            if(dir == N){
                return E;
            }
            else if(dir == E){
                return N;
            }
            else if(dir == S){
                return W;
            }
            else if(dir == W){
                return S;
            }
            break;
        case '\\':
            if(dir == N){
                return W;
            }
            else if(dir == E){
                return S;
            }
            else if(dir == S){
                return E;
            }
            else if(dir == W){
                return N;
            }
            break;
        case '=':
            if(dir == N){
                return S;
            }
            else if(dir == E){
                return W;
            }
            else if(dir == S){
                return N;
            }
            else if(dir == W){
                return E;
            }
            break;
    }
	return 0;
}

/***************************************************************************
** Move the box.
** Box is a struct. Three elements are in the Box which are direction and the
** position x and postition y.
*/

void Move(int dir,int x,int y){
    if (count++ == maxteps) {
        printf("End of simulation.\n");
        return;
    }
	DisplayMap();
    /*For each direction, and operation it will do seperate stuff.*/
	if(dir == E){
		x -= 1;
		if(IsLegal(x, y) == 0){
			printf("End of simulation.\n");
            return;
		}
		if(map[y][x] == '@'){
			x -= 5;
            /*Check to see if it is out of the map.*/
            if (!IsLegal(x, y)) {
                printf("End of simulation.\n");
                return;
            }else{
                DisplayMap();
            }
        }
	}
	else if(dir == W){
		x += 1;
		if(IsLegal(x, y) == 0){
			printf("End of simulation.\n");
            return;
		}
		if(map[y][x] == '@'){
			x += 5;
            if (!IsLegal(x, y)) {
                printf("End of simulation.\n");
                return;
            }else{
                DisplayMap();
            }
        }
	}
	else if(dir == N){
		y += 1;
		if(IsLegal(x, y) == 0){
			printf("End of simulation.\n");
            return ;
		}
		if(map[y][x] == '@'){
			y += 5;
            if (!IsLegal(x, y)) {
                printf("End of simulation.\n");
                return;
            }else{
                DisplayMap();
            }
        }
	}
	else {
		y -= 1;	
		if(IsLegal(x, y) == 0){
			printf("End of simulation.\n");
            return;
		}
		if(map[y][x] == '@'){
            y -= 5;
            if (!IsLegal(x, y)) {
                printf("End of simulation.\n");
                return;
            }else{
                DisplayMap();
            }
        }
	}
	if(IsLegal(x,y) == 0){
		printf("End of simulation.\n");
        return;
	}
	if(map[y][x] == '.'){
		map[y][x] = '1';
		Move(dir, x, y);
	}
	else if(map[y][x] == '/'){
		dir = GetDir(dir, '/');
		Move(dir, x, y);
	}
	else if(map[y][x] == '\\'){
		dir = GetDir(dir, '\\');
		Move(dir, x, y);
	}
	else if(map[y][x] == '='){
		dir = GetDir(dir, '=');
		Move(dir, x, y);
	}
	else if(map[y][x] == '@'){
		Move(dir, x, y);
	}
	else if(map[y][x] >= '1' && map[y][x] <= '9'){
		map[y][x] += 1;
		Move(dir, x, y);
	}
	else if(map[y][x] >= 'A' && map[y][x] <= 'Z'){
		if(first == 0){
			first = 1;
			tempBox.direction = dir;
			tempBox.x = x;
			tempBox.y = y;
		}
		FindNext(&x, &y, map[y][x], dir);
		Move(dir, x, y);
	}
}

/****************************************************************************
** Display the map.
** Because the map would be used for mutiple times. This function basically 
** print everything currently of the map.
*/

void DisplayMap(){
	int i = 0;
	int j = 0;
	for(i = 0; i < row; i++){
		for(j = 0; j < col; j++){
			printf("%c", map[i][j]);
		}
		printf("\n");
	}
	printf("\n");
}

/****************************************************************************
** Initialize the box.
** make the box at the initial state.
*/
void initBox(char dir, int pos){
	if(dir == 'N'){
		box.direction = N;
		box.x = pos - 1;
		box.y = 0;
	}
	else if(dir == 'W'){
		box.direction = W;
		box.x = 0;
		box.y = pos - 1;
	}
	else if(dir == 'E'){
		box.direction = E;
		box.x = col - 1;
		box.y = pos - 1;
	}
	else if(dir == 'S'){
		box.direction = S;
		box.x = pos - 1;
		box.y = row - 1;
	}
	else {
		error(ERROR_SIZE, "");
		return ;
	}
    /*Check the first shot is . or not*/
    if (map[box.y][box.x] == '.' ) {
        map[box.y][box.x] = '1';
        Move(box.direction, box.x, box.y);
    }else{
        dir = GetDir(box.direction, map[box.y][box.x]);
        dir = GetDir(box.direction, map[box.y][box.x]);
        Move(dir, box.x, box.y);
    }
}

/***************************************************************************
** Allocate memeory for the map
** make a 2-D array.
*/

char** alloc_map(int row, int col){
    int i;
    char** map = malloc(sizeof(char*)*row);
    for (i=0; i < row; ++i){
        map[i]= malloc(sizeof(char)*col);
    }
    return map;
}

/***************************************************************************
** Initialize the the map
** Check the map error within this function.
*/

void initMap(char* name, char* mode){
	int i = 0;
	int j = 0;
	char ch;
    static char buffer[10];
    int res;

	fp = fopen(name, mode);
	if(!fp){
		error(3, "");
	}
    fgets(buffer, 10, fp);
    res = sscanf(buffer, "%d %d", &row, &col);
    if (res != 2) {
        error(ERROR_DIME, "");
    }
    if ((row == 0) || ((int)row < 0) || (col == 0) || ((int)col < 0)){
        error(ERROR_DIME, "");
    }
	map = alloc_map(row, col);
    
	for(i = 0; i < row; i++){
		ch = fgetc(fp);
		for(j = 0; j < col; j++){
            if((ch>='A' && ch <= 'Z') || ch=='@' ||ch=='\\'
               || ch=='/' || ch=='=' || ch=='.'){
                map[i][j] = ch;
                ch = fgetc(fp);
            }else{
                error(ERROR_CHAR, "");
            }
		}
	}
	if(fgetc(fp) != EOF){
		error(ERROR_FILE, "");
		return ;
	}
    CheckMap();
    DisplayMap();
    fclose(fp);
}

/***************************************************************************
** Find next letter.
** Use for the box to find next Letter in the map.
*/

int FindNext(int *x, int *y, char ch, int dir){
	int i = 0;
	int j = 0;
	for(i=0; i < row; i++)
		for(j=0; j < col; j++)
			if(map[i][j] == ( ch + 1 )){
                /*Because the teleport would not change the direction*/
				if(dir == N){
					*y = i;
					*x = j;
				}
				else if(dir == S){
					*y = i;
					*x = j;
				}
				else if(dir == W){
					*x = j;
					*y = i;
				}
				else{
					*x = j;
					*y = i;
				}
				return 1;
			}
			return 0;
}

/****************************************************************************
** IsLegal, boolean function.
** This function is to use to see if the box is out of the map.
*/

int IsLegal(int x, int y){
	if(x < 0 || y < 0 || x >= col || y >= row){
		return 0;
	}
	return 1;
}

/****************************************************************************
** process_command_line.
** Checks the arguments are as expected and sets global variables
** appropriately. If arguments are not as expected, a usage error is
** produced and the program exits.
*/

int process_command_line (int argc, char* argv[]){
    char* res;
    int maxsteps;
    mapFileName = argv[1];
    if(argc > 0){
        if(argc == 3){
            res = argv[2];
            maxsteps = atoi(res);
            /*Check the maxsteps*/
            if (maxsteps >= 1000 || maxsteps<=0) {
                error(2, "");
            }
        }
        else if (argc == 2) {
            maxsteps = 10;
        }
        else {
            error(1, "");
        }
    }
    return maxsteps;
    
}

/*****************************************************************************
** error
**
** Prints an error message and exits with the given exit status.
** 7 errors in total.
*/

void error(int code, char* arg){
    switch(code){
        case 1:
            fprintf(stderr, "Usage: thebox mapfile [maxsteps]\n" );
            break;
        case 2:
            fprintf(stderr, "Bad max steps.\n");
            break;
        case 3:
            fprintf(stderr, "Missing map file.%s\n", arg);
            break;
        case 4:
            fprintf(stderr, "Bad map dimensions.\n");
            break;
        case 5:
            fprintf(stderr, "Bad map char.\n");
            break;
        case 6:
            fprintf(stderr, "Map file is the wrong size.\n");
			break;
		case 7:
            fprintf(stderr, "Missing letters.\n");
			break;
    }
    exit(code);
}

/*****************************************************************************
** The following tow functions is use for teleport
**
**
*/
void GetNextNext(char ch){
	int i,j;
	for(i = 0; i < row; i++){
		for(j = 0; j < col; j++){
			if(map[i][j] == (ch+1)){
                first ++;
				return ;
			}
		}
    }
	error(ERROR_MISS, "");
}

void GetLast(int x,int y,int dir){
	if(IsLegal(x, y) == 0 && first >= 1){
		if(dir == E){
			tempBox.x -= 1;
		}
		else if(dir == W){
			tempBox.x += 1;
		}
		else if(dir == N){
			tempBox.y += 1;
		}
		else{
			tempBox.x -= 1;
		}
		map[tempBox.y][tempBox.x] = '1';
		DisplayMap();
	}
}
/*****************************************************************************
** Check the missing letter in the map.
** Make a index value, if it does not match the 
** str[index - 1] - str[0] != (index - 1). It should return the error.
*/
void CheckMap()
{
	int i, j;
	char str[1000];
	int index = 0;
	for(i = 0; i < row; i++)
		for(j = 0; j < col; j++){
			if(map[i][j] >= 'A' && map[i][j] <= 'Z'){
				str[index] = map[i][j];
				index++;
			}
		}
    /*If there is no letter in the map, the map does not need to be checked.*/
    if (index == 0) {
        return ;
    }
    if (index == 1) {
        error(ERROR_MISS, "");
    }
	for(i = 0; i < index - 1; i++){
		for(j = i + 1; j < index; j++){
			if(str[i] == str[j]){
				error(ERROR_MISS, "");
			}
			else if(str[i] > str[j]){
				char ch = str[i];
				str[i] = str[j];
				str[j] = ch;
			}
		}
    }
	if(str[index - 1] - str[0] != (index - 1)){
		error(ERROR_MISS, "");
	}
}






