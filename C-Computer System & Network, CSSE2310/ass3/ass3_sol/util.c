#include "util.h"
#include <stdlib.h>

// This code might deallocate the buffer you pass it
// lim will be updated to reflect new capacity
// returns true if valid line, false on no line available.
bool get_noncomment_line(char** buffer, uint* lim, FILE* f) {
    int c;
    do {
	c=fgetc(f);
	if (c==EOF) {
	    return false;
	}
	if (c=='#') { // skip the rest of this line
	    do {
	        c=fgetc(f);
	    } while ((c!=EOF) && (c!='\n'));
	} else {
	    // So We have a non-comment line
	    // and yes, using fgets would be more efficient
	    uint i=0; 
	    do {
	        (*buffer)[i]=(char)c;
	        if (i==*lim-1) { // we just wrote into the last slot
		    *lim+=80;
		    *buffer=realloc(*buffer,*lim);
		    if (*buffer==0) { // no real recovery here
		        return false;	
		    }
		} 
		i++;
		c=fgetc(f);
	    } while ((c!=EOF) && (c!='\n'));
	    (*buffer)[i]='\0';	    
	    return true;
	}
    } while (1);
}

void getDirVector(char d, int* deltarow, int* deltacol) {
    switch (d) {
      case 'N': *deltarow=-1; *deltacol=0; break;
      case 'S': *deltarow=1; *deltacol=0; break;      
      case 'E': *deltarow=0; *deltacol=1; break;
      case 'W': *deltarow=0; *deltacol=-1; break;    
      default: break;
    }
}