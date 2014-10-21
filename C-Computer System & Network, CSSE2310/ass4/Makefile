CFLAGS = -Wall -pedantic -pthread -std=gnu99 -g
all: serv499 client499 test
serv499: serv499.c
	gcc $(CFLAGS) serv499.c -o $@
client499:
	gcc $(CFLAGS) client499.c -o $@
clean:
	rm -rf serv499 client499 *.o testres.*
test:
	testa4.sh