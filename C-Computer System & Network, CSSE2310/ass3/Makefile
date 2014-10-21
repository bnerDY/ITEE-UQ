CFLAGS = -Wall -pedantic -std=gnu99 -g
all: handler fromfile waller simple slow slow2 test 
handler: handler.c shared.o
	gcc $(CFLAGS) handler.c shared.o -o $@
waller: waller.c shared.o
	gcc $(CFLAGS) waller.c shared.o -o $@
simple: simple.c shared.o
	gcc $(CFLAGS) simple.c shared.o -o $@
fromfile: fromfile.c shared.o
	gcc $(CFLAGS) fromfile.c shared.o -o $@
slow: slow.c shared.o
	gcc $(CFLAGS) slow.c shared.o -o $@
slow2: slow2.c shared.o
	gcc $(CFLAGS) slow2.c shared.o -o $@
shared.o: shared.c shared.h
	gcc $(CFLAGS) -c shared.c shared.h
clean: 
	rm -rf handler fromfile waller simple slow slow2 *.o testres.*
test:
	testa3.sh