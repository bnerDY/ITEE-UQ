#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <math.h>
#include "agent.h"
#include "util.h"

void cleanup_agent(agent_t* a) {
    // close files etc
    fclose(a->to);
    fclose(a->from);
    free(a->argv);
    free(a->args);
    
    
    if (a->pid<1) {
       fprintf(stderr, "About to kill %d\n", a->pid);
       
       
       
    } else {
    
    // Just in case the streams closing didn't do it
    kill(a->pid, SIGTERM);	// this is impolite
    waitpid(a->pid, 0, 0);
    
    }
}

// format is %u %u %c ........
// with exactly one space separating
// If someone is silly enough to put extra spaces
// in their line, then the agent will get extra args
bool init_agent(agent_t* a, const char* line) {
    a->args=malloc(sizeof(char)*(strlen(line)+1));
    strcpy(a->args, line);
    if (sscanf(line,"%u%*c%u%*c%c", &(a->r), &(a->c), &(a->s))!=3) {
        free(a->args);
        return false;
    }   
    // now we need to work out where the name of the program starts
    // we'll use logs to count the number of digits
    
    int pos=3+(int)(log10(a->r))+1+(int)(log10(a->c))+1+1;
    if (strlen(line)<=pos) {
        free(a->args);
        return false;
    }
          // now count how many args
    char* temp=a->args+pos;
    uint argc=1;
    while (*temp!='\0') {
        if (*temp==' ') {
	    argc++;
	}
        temp++;
    }
    a->argc=argc;
    a->argv=malloc(sizeof(char*)*(argc+1));
    a->argv[argc]=0;
    temp=a->args+pos;
    argc=0;
    a->argv[argc]=temp;
    while (*temp!='\0') {
        if (*temp==' ') {
	    *temp='\0';
	    argc++;
	    a->argv[argc]=temp+1;
	}
        temp++;
    } 
    a->r--;
    a->c--; 
    a->status=0;
    a->alive=false;
    a->pid=0;
    return true;
}

typedef struct listtmpname{
    agent_t* data;
    struct listtmpname* next;
} list_t;

void destroy_list(list_t* l) {
    list_t* t;  
    while (t=l, l!=0) {
        l=l->next;	
        cleanup_agent(t->data);
	free(t);
    }
}

// Also destroys the list
agent_t** make_array(list_t* l, uint* size) {
    list_t* t=l;
    uint count=0;
    while (t!=0) {
        count++;
        t=t->next;
    }
    *size=count;
    agent_t** result=malloc(sizeof(agent_t*)*(*size));
    for (uint i=0;i<count;++i) {
        t=l;
	l=l->next;
	result[i]=t->data;
	free(t);
    }
    return result;
}


// We could do this with a resizeable array but let's use a list instead
// read a line extract the agent info and add it to the list
// repeat until we have all the info
// convert the list to an array and we are done
// returns an agent* array and sends back the number of records
agent_t** read_agentfile(FILE* f, uint* numrecords) {
    char* lin=malloc(sizeof(char)*80);
    uint limit=80;
    list_t* head=0;
    list_t* tail=0;
    memset(lin, '\0', 80);      // just to keep valgrind happy for now
    while (get_noncomment_line(&lin, &limit, f)) {
        list_t* node=malloc(sizeof(list_t));
	agent_t* ag=malloc(sizeof(agent_t));
	node->data=ag;
	node->next=0;	
	if (!init_agent(ag, lin)) {
	    destroy_list(head);
	    free(lin);
	    return 0;
	}
	   // now to splice it into the list
	if (head==0) {
	    head=node;
	    tail=node;
	} else {
	    tail->next=node;	// attach to tail
	    tail=node;		// update tail
	}
        free(lin);
        lin=malloc(sizeof(char)*80);
	limit=80;
    }
        // need to clean up the last alloc
    free(lin);
    if (head==0) {
       *numrecords=0;
       return 0;	// the list is empty  
    }
        // Ok now we have a list, convert it to an array
    agent_t** result=make_array(head, numrecords);
    return result;
}
