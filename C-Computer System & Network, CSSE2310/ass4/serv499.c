#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include <pthread.h>
#include <netdb.h>
#include <string.h>

#define MAXHOSTNAMELEN 128

typedef struct {
    char var;
    char suit;
} Card;

typedef struct {
    char* playerName;
    char* gameName;
    int pid;
    char* hostName;
    Card c[13];
} Player;

typedef struct {
    Player p[4];
    char* gameName;
} Room;
/*Assume 100 players*/
Player p[100];

/*Handle the incoming connection requests,*/
int open_listen(int port)
{
    int fd;
    struct sockaddr_in serverAddr;
    int optVal;
    /*  Create TCP socket */
    fd = socket(AF_INET, SOCK_STREAM, 0);
    if(fd < 0) {
        fprintf(stderr, "Port Error\n");
        exit(5);
    }
    /* Set the option to reuse the socket address immediately */
    optVal = 1;
    if(setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &optVal, sizeof(int)) < 0) {
        fprintf(stderr, "Port Error\n");
        exit(5);
    }
    /* Set up the address structure for the server side of the connection
     * - any local IP address is OK (INADDR_ANY)
     * - given port number - convert to network byte order
     */
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
    /* Bind our socket to the given address (IP address and port number) */
    if(bind(fd, (struct sockaddr*)&serverAddr, 
	    sizeof(struct sockaddr_in)) < 0) {
        fprintf(stderr, "Port Error\n");
        exit(5);
    }
    /* Start listening for incoming connection requests - queue up to SOMAXCONN
     * of them
     */
    if(listen(fd, SOMAXCONN) < 0) {
        fprintf(stderr, "Port Error\n");
        exit(5);
    }
    return fd;
}

/*Read the deck file*/
void read_deck(char* filename) {
    FILE* f = fopen(filename, "r");
    int count = 13;
    int pNumber = 4;
    int i, j;
    if (!f) {
        fprintf(stderr, "Deck Error\n");
        exit(6);
    } else {
        for (i = 0; i < count; i++) {
            for (j = 0; j < pNumber; j++) {
                p[j].c[i].var = fgetc(f);
                if (p[j].c[i].var == EOF) {
                    fprintf(stderr, "Deck Error\n");
                    exit(6);
                }
                p[j].c[i].suit = fgetc(f);
            }
        }
    }
    if (fgetc(f) != EOF) {
        fprintf(stderr, "Deck Error\n");
        exit(6);
    }
    fclose(f);
}

/*Make the response to client*/
void process_connections(int fdServer, char* msg)
{
    int fd;
    char buffer[1024];
    struct sockaddr_in fromAddr;
    socklen_t fromAddrSize;
    int error;
    char hostname[MAXHOSTNAMELEN];
    //pthread_t threadId;
    int num = 0;
    int fdc[4];
    while(1) {
        fromAddrSize = sizeof(struct sockaddr_in);
        /* Block, waiting for a connection request to come in and accept it.
         * fromAddr structure will get populated with the address of the client
         */
        fd = accept(fdServer, (struct sockaddr*)&fromAddr, &fromAddrSize);
        if(fd < 0) {
            fprintf(stderr, "Port Error\n");
            exit(5);
        }
        error = getnameinfo((struct sockaddr*)&fromAddr, fromAddrSize, 
		hostname, MAXHOSTNAMELEN, NULL, 0, 0);
        if(error) {
            fprintf(stderr, "Port Error\n");
        } else {
            printf("Accepted connection from %s (%s), port %d\n",
                    inet_ntoa(fromAddr.sin_addr), hostname,
                    ntohs(fromAddr.sin_port));
            char* signalString;
            signalString = (char*)malloc(strlen(msg) + 1);
            sprintf(signalString, "M%s", msg);
            write(fd, signalString, strlen(signalString));
            read(fd, buffer, 1024);
            sscanf(buffer, "%s\n%s\n", p[num].playerName, p[num].gameName);
            p[num].pid = fd;
            fdc[num] = fd;
            printf("%s %s\n", p[num].playerName, p[num].gameName);
            num++;
            memset(buffer, 0, sizeof(buffer));//reset the buffer
        }
        //pthread_create(&threadId, NULL, client_thread, (void*)(int64_t)fd);
        //pthread_detach(threadId);
        if (num == 4) {
            char* initHand;
            initHand = (char*)malloc(14);
        }
    }
    /*Create game for 4 peoples currently.*/
}


int main(int argc, char* argv[])
{
    int portnum;
    int fdServer;
    char* greetMsg;
    if(argc != 4) {
        fprintf(stderr, "Usage: serv499 port greeting deck\n");
        exit(1);
    }
    
    /* Get the port number from the arguments and check range */
    portnum = atoi(argv[1]);
    if(portnum < 1 || portnum > 65535) {
        fprintf(stderr, "Invalid Port\n");
        exit(4);
    }
    greetMsg = argv[2];
    read_deck(argv[3]);
    /* Start listening on the given port */
    fdServer = open_listen(portnum);
    process_connections(fdServer, greetMsg);
    return 0;
}
