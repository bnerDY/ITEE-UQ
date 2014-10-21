#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <limits.h>

#define BADARGS 1
#define BADMAX 2
#define NOMAP 3
#define BADDIM 4
#define BADCHAR 5
#define BADDATA 6
#define BADSEQ 7

typedef struct {
    int height;
    int width;
    int maxsteps;    
    char** data;
    char** display;
    int posx[26];	/* positions of letters to save time later */
    int posy[26];
} grid_type;


void error(int i) {
    char* e;
    switch (i) {
      case BADARGS: e="Usage: thebox mapfile [maxsteps]";break;   
      case BADMAX: e="Bad max steps.";break;   
      case NOMAP: e="Missing map file.";break;   
      case BADDIM: e="Bad map dimensions.";break;   
      case BADCHAR: e="Bad map char.";break;   
      case BADDATA: e="Map file is the wrong size.";break;   
      case BADSEQ: e="Missing letters.";break;   
      default:
	e="?????";
    }
    fprintf(stderr, "%s\n", e);
}


grid_type* alloc_grid(int h, int w) {
    int i;
    grid_type* g;
    if ((h<1) || (w<1)) {
        return 0;
    }
    g=malloc(sizeof(grid_type));
    g->height=h;
    g->width=w;
    g->data=malloc(sizeof(char*)*h);
    for (i=0;i<h;++i) {
        g->data[i]=malloc(sizeof(char)*(w+1));
	g->data[i][w]='\0';	/* to make display easier */
    }
    g->display=malloc(sizeof(char*)*h);
    for (i=0;i<h;++i) {
        g->display[i]=malloc(sizeof(char)*(w+1));
	g->display[i][w]='\0';	/* to make display easier */
    }
    for (i=0;i<26;++i) {
        g->posx[i]=-1;	/* sentinel */
        g->posy[i]=-1;
    }
    g->maxsteps=1;
    return g;
}

void free_grid(grid_type* g) {
    int i;
    for (i=0;i<(g->height);++i) {
        free(g->data[i]);
	free(g->display[i]);
    } 
    free(g->data);
    free(g->display);
    free(g);
}

void show_grid(grid_type* g) {
    int i;
    for (i=0;i<g->height;++i) {
        printf("%s\n",g->display[i]);      
    }
    printf("\n");
}

/* copies the contents of the orignal map to the display map */
void reset_display(grid_type* g) {
    int i;
    for (i=0;i<g->height;++i) {
        memcpy(g->display[i], g->data[i], (g->width+1)*sizeof(char));
    }
}

void stamp(char* c) {
    if ((*c=='/') || (*c=='\\') || (*c=='=') || (*c=='@') || (*c=='9') || ((*c>='A') && (*c<='Z'))) {
        return;
    }
    if (*c=='.') {
        *c='1';
        return;
    }
    (*c)++;	/* it must be a number < 9 so increment */
}



/* This could have been made more efficient by keeping the letter positions in an array
 * but if you didn't want to do that you can do this */
void set_pos(grid_type* g, char letter, int* r, int* c) {
    int index=letter-'A';  /* Next letter */
    do {
        index=(index+1)%26;
    } while (g->posx[index]==-1);
    *r=g->posy[index];
    *c=g->posx[index];
}


void do_shot(grid_type* g, char face, int r, int c) {
    int steps=0;
    int deltax=0;
    int deltay=0;
    int buffer=0;	/* for swapping values */
    switch (face) {	/* remember this is the face not the direction */
      case 'N': deltay=1; break;
      case 'S': deltay=-1; break;
      case 'E': deltax=-1; break;
      case 'W': deltax=1; break;
      default: break;
    }
    while ((r>=0) && (r<g->height) && (c>=0) && (c<g->width) && (steps<g->maxsteps)) {   
           /* stamp the current position */
        stamp(&(g->display[r][c]));
           /* Now perform the action for this type */
        switch (g->data[r][c]) {
	  case '/':  buffer=deltax; deltax=-deltay; deltay=-buffer; r+=deltay; c+=deltax;break;
	  case '\\': buffer=deltax; deltax=deltay; deltay=buffer; r+=deltay; c+=deltax;break;
	  case '=':  deltax*=-1; deltay*=-1; r+=deltay; c+=deltax;break;
	  case '@':  r+=5*deltay; c+=5*deltax; break;
	  case '.':  r+=deltay; c+=deltax;break;
	  default:	/* which must be a letter */
	    set_pos(g, g->data[r][c], &r, &c);
	    r+=deltay; c+=deltax;
	}
	steps++;
	show_grid(g);
    }
}

/* Loops prompting the user until a valid shot is entered or EOF
 * Returns: 1 for valid, 0 for eof */
char get_coords(grid_type* g, char* dir, int* pos) {
    char dummy;
    char line[20];	/* more than we need */
    while(printf("(side pos)>"),fgets(line, 18,stdin), !feof(stdin)) {
	if ((line[0]!='\n') && (line[strlen(line)-1]!='\n')) {
	    while (dummy=fgetc(stdin), ((dummy!='\n') && !feof(stdin))){}
	    continue;
	}
	if (sscanf(line, "%c%d%c",dir, pos, &dummy)==3)
	{
	    if ((*dir!='N') && (*dir!='S') && (*dir!='W') && (*dir!='E')) {
		continue;	  
	    }
	    if ((*pos<1) || (*pos>1000)) {
		continue;
	    }
	    if (dummy!='\n') {	/* we want to gobble the rest of the line */
		while ((dummy!='\n') && !feof(stdin)) {
		    scanf("%c", &dummy);	/* yes I could have used getc here */
		}
		continue;
	    }
		/* ok, we have a direction and a number. Is the number good? */
	    if ((*dir=='N') || (*dir=='S')) {
	      if ((*pos<1) || (*pos>(g->width))) {
		  continue;
	      }
	    } else {
	      if ((*pos<1) || (*pos>(g->height)))
	      {
		  continue;
	      }
	    }
	    return 1;
	}
    }
    return 0;	/* if we got here it must be eof */
}

void start_shot(grid_type* g) {
    char dir;
    int pos;
    int startrow=0;	/* tricky because from the user's view coords are 0 based */
    int startcol=0;
    if (!get_coords(g, &dir, &pos)) {
        return;
    }
    switch (dir) {
      case 'N': startrow=0; startcol=pos-1; break;  
      case 'S': startrow=g->height-1; startcol=pos-1; break;  
      case 'E': startrow=pos-1; startcol=g->width-1; break;  
      case 'W': startrow=pos-1; startcol=0; break;  
      default: break;	  
    }
    do_shot(g, dir, startrow, startcol);
    printf("End of simulation.\n");    
}


/* This function is more convoluted than it would normally need to be but
 * I am demonstrating paranoia */
grid_type* read_map(FILE* f, int *err) {
                	/* if we weren't worried about leading spaces we could use:
                    fscanf(f, "%d%c%d%c"   */
    char buff[80];	/* a bit more than we need */
    grid_type* g;
    char letters[26];  /* to keep track of which letters we have seen */
    short lcount=0;
    char c1=0,c2=0;
    int h,w;
    int first=0;
    int i,j;
    *err=0;
    memset(letters,0,sizeof(char)*26);
    fgets(buff, 80, f);
    if ((buff[0]=='0') || 
        (buff[strlen(buff)-1]!='\n')) {	/* we didn't read complete line */
        *err=BADDIM;
        return 0;
    }
    if (isspace(buff[0])) {
        *err=BADDIM;
	return 0;
    }
    if ((sscanf(buff,"%d%c%d%c", &h, &c1, &w, &c2)!=4) || (c1!=' ') || (c2!='\n')) {
        *err=BADDIM;
        return 0;
    }
    if ((h<1) || (w<1)) {
        *err=BADDIM;
	return 0;
    }
    if ((h>=1000) || (w>=1000)) {
	*err=BADDIM;
	return 0;
    }
         /* at this point we have valid dimensions */
    g=alloc_grid(h,w);
         /* Now we could read this a line at a time but we have a rigid structure for the file */
    for (i=0;(i<h) && (*err==0);++i) {
        for (j=0;j<w;++j) {
	    c1=fgetc(f);
	    if (feof(f)) {
	        *err=BADDATA;
	        break;
	    }
	    /* now check to see if the char is valid */
	    if ((c1!='.') && (c1!='/') && (c1!='\\') && (c1!='=') && 
	        (c1!='@') && !((c1>='A') && (c1<='Z'))) {
	        *err=BADCHAR;
	        break;
	    }    
	    g->data[i][j]=c1;
	}
	if (*err!=0) {
	    break;
	}
	/* now we read the newline*/
	c1=fgetc(f);
	if ((c1!='\n') || (feof(f))){
	    *err=BADDATA;
	    break;
	}
    }
    if ((i!=h) || (j!=w)) {	/* we didn't complete the loops */
        free_grid(g);
	return 0;
    }
    c1=fgetc(f);
    c1=fgetc(f);
    if (!feof(f)) {	/* this means the read call found something past the last row */
        free_grid(g);
        *err=BADDATA;
	return 0;
    }
    for (i=0;(i<h) && (*err==0);++i) {
        for (j=0;j<w;++j) {
            char c1=g->data[i][j];
	    if ((c1>='A') && (c1<='Z')) {
		if (letters[c1-'A']) {	/* already seen one */
		    *err=BADSEQ;
		    break;
		}
		letters[c1-'A']=1;
		g->posx[c1-'A']=j;
		g->posy[c1-'A']=i;
		lcount++;
	    }
	}
    }
        /* we completed the loops but now we need to make sure that any letters we have make sense */
    if (*err || (lcount==1)) {
        free_grid(g);
        *err=BADSEQ;
	return 0;
    }
       /* first find the first letter */
    for (i=0;i<26;++i) {
        if (letters[i]) {
	    first=i;
	    break;
	}      
    }
    for (i=first;i<26;++i) {
        if (letters[i]) {
	    lcount--;
	} else {
	    break;
	}
    }
    if (lcount!=0) {	/* broken sequence */
        *err=BADSEQ;
	return 0;
    }
    reset_display(g);
    return g;
}

int main(int argc, char** argv) {
    int maxsteps=10;
    FILE* mapfile=0;
    grid_type* map=0;
    int merr;
    if ((argc<2) || (argc>3)) {
        error(BADARGS);
        return BADARGS;
    }
    if (argc==3) {	/* user supplied maxsteps */
        char* err;
        long int v=strtol(argv[2], &err, 10);
        if ((*err!='\0') || (v<1) || (v>=1000)) {	/* did not process the whole argument */
            error(BADMAX);
	    return BADMAX;
	}
	maxsteps=(int)v;
    }
    mapfile=fopen(argv[1],"r");
    if (mapfile==0) {
        error(NOMAP);
	return NOMAP;
    }
    map=read_map(mapfile, &merr);
    if (!map) {
        error(merr);
        return merr;
    }
    map->maxsteps=maxsteps;
    do {
        reset_display(map);
        show_grid(map);
	start_shot(map);

    } while (!feof(stdin));
    return 0;
}
