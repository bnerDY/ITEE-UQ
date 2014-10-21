#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>
#include <string.h>
/*card struct*/
typedef struct {
    char var;
    char suit;
} Card;

/*client struct*/
typedef struct {
    char* playerName;
    char* gameName;
    int port;
    char* hostName;
} Client;

/* Convert hostname to an address */
struct in_addr* name_to_ip_addr(char* hostname)
{
    int error;
    struct addrinfo* addressInfo;
    error = getaddrinfo(hostname, NULL, NULL, &addressInfo);
    if(error) {
        return NULL;
    }
    /* Extract the IP address from the address structure and return it */
    return &(((struct sockaddr_in*)(addressInfo->ai_addr))->sin_addr);
}

 /* Attempt to connect to that remote address */
int connect_to(struct in_addr* ipAddress, int port)
{
    struct sockaddr_in socketAddr;
    int fd;
    /* Create TCP socket */
    fd = socket(AF_INET, SOCK_STREAM, 0);
    if(fd < 0) {
        perror("Error creating socket");
        exit(1);
    }
    /* Create structure that represents the address (IP address and port
     * number) to connect to
     */
    socketAddr.sin_family = AF_INET;
    socketAddr.sin_port = htons(port);
    socketAddr.sin_addr.s_addr = ipAddress->s_addr;
    if(connect(fd, (struct sockaddr*)&socketAddr, sizeof(socketAddr)) < 0) {
        perror("Error connecting");
        exit(1);
    }
    return fd;
}

/* Read all data from file descriptor until end of file
* and print to standard output
*/
void get_and_output_http_response(int fd)
{
    char buffer[1024];
    char ch;
    Card c[13];
    
    FILE* dataIn = fdopen(fd, "r");
    while(fgets(buffer, 1024, dataIn) != NULL) {
        ch = buffer[0];
        if (ch == 'M') {
            memmove(buffer, buffer + 1, strlen(buffer));
            printf("%s\n", buffer);
            memset(buffer, 0, sizeof(buffer));//reset the buffer
        } else if (ch == 'H') {
            memmove(buffer, buffer + 1, strlen(buffer));
            printf("%s\n", buffer);
            for(int i = 0; i < 13; i++) {
                c[i].var = buffer[i * 2];
                c[i].suit = buffer[i * 2 + 1];
            }
            memset(buffer, 0, sizeof(buffer));//reset the buffer
        } else if (ch == 'B') {
            memmove(buffer, buffer + 1, strlen(buffer));
            memset(buffer, 0, sizeof(buffer));
        } else if (ch == 'L') {
            memmove(buffer, buffer + 1, strlen(buffer));
            printf("Lead> ");
            memset(buffer, 0, sizeof(buffer));
        } else if (ch == 'P') {
            memmove(buffer, buffer + 1, strlen(buffer));
            memset(buffer, 0, sizeof(buffer));
        } else if (ch == 'A') {
            memmove(buffer, buffer + 1, strlen(buffer));
            memset(buffer, 0, sizeof(buffer));
        } else if (ch == 'T') {
            memmove(buffer, buffer + 1, strlen(buffer));
            memset(buffer, 0, sizeof(buffer));
        } else if (ch == 'O') {
            memmove(buffer, buffer + 1, strlen(buffer));
            memset(buffer, 0, sizeof(buffer));
            exit(0);
        } else {
            fprintf(stderr, "Protocol Error.\n");
            exit(6);
        }
    }
}



/*Check the command line and arguments*/
Client process_command_line(int argc, char* argv[]) {
    Client tempUser;
    if (argc < 4) {
        fprintf(stderr, "Usage: client499 name game port [host]\n");
        exit(1);
    } else if (argc == 4) {
        tempUser.playerName = argv[1];
        tempUser.gameName = argv[2];
        tempUser.port = atoi(argv[3]);
        if (tempUser.port < 1 || tempUser.port > 65535) {
            fprintf(stderr, "Invalid Arguments.\n");
            exit(4);
        }
        if (strcmp(tempUser.playerName, "") == 0) {
            fprintf(stderr, "Invalid Arguments.\n");
            exit(4);
        }
        if (strcmp(tempUser.gameName, "") == 0) {
            fprintf(stderr, "Invalid Arguments.\n");
            exit(4);
        }
        tempUser.hostName = "localhost";
    } else if (argc == 5) {
        tempUser.playerName = argv[1];
        tempUser.gameName = argv[2];
        tempUser.port = atoi(argv[3]);
        if (tempUser.port < 1 || tempUser.port > 65535) {
            fprintf(stderr, "Invalid Arguments.\n");
            exit(4);
        }
        if (strcmp(tempUser.playerName, "") == 0) {
            fprintf(stderr, "Invalid Arguments.\n");
            exit(4);
        }
        if (strcmp(tempUser.gameName, "") == 0) {
            fprintf(stderr, "Invalid Arguments.\n");
            exit(4);
        }
        tempUser.hostName = argv[4];
    } else {
        fprintf(stderr, "Usage: client499 name game port [host]\n");
        exit(1);
    }
    return tempUser;
}

int main(int argc, char* argv[]) {
    int fd;
    struct in_addr* ipAddress;
    Client user;
    user = process_command_line(argc, argv);
    
    /* Convert our hostname to an IP address */
    ipAddress = name_to_ip_addr(user.hostName);
    if(!ipAddress) {
        fprintf(stderr, "Bad Server.\n");
        exit(2);
    }
    fd = connect_to(ipAddress, user.port);
    //send_HTTP_request(fd, "/", user.hostName);
    char* identifyString;
    identifyString = (char*)malloc(strlen(user.playerName)
            + strlen(user.gameName));
    sprintf(identifyString, "%s\n%s\n", user.playerName, user.gameName);
    write(fd, identifyString, strlen(identifyString));
    while (1) {
        get_and_output_http_response(fd);
    }
    close(fd);
    return 0;
}

