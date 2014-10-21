#ifndef ERRS_H
#define ERRS_H

#define SUCCESS 0
#define ERR_ARGS 1
#define ERR_MAX 2
#define ERR_MAPFILE 3
#define ERR_MAPREAD 4
#define ERR_AGFILE 5
#define ERR_AGREAD 6
#define ERR_START 7
#define ERR_WALL 8
#define ERR_CRASH 9
#define ERR_STEPS 10
#define ERR_REPLY 11
#define ERR_CLOSE 12
#define ERR_SIGCLOSE 13
#define ERR_SIGINT 14

void print_msg(int m, char c, int d);


#endif