OPTS=--std=gnu99 -pedantic -Wall -Werror -g -lm
OBJS=util.o main.o sim.o agent.o map.o errs.o clutil.o 
PROGS=slow2 slow simple handler waller fromfile

.PHONY=clean all

all: $(PROGS)


slow2:	slow2.c clutil.h types.h map.h clutil.o types.h map.o
	gcc $(OPTS) slow2.c clutil.o map.o -o slow2

slow: slow.c clutil.o types.h map.o
	gcc $(OPTS) slow.c clutil.o map.o -o slow

simple: simple.c map.h types.h util.h clutil.h map.o util.o clutil.o
	gcc $(OPTS) simple.c map.o util.o clutil.o -o simple

handler: $(OBJS)
	gcc $(OPTS) $(OBJS) -o handler
	
waller: wallmain.o map.o clutil.o util.o
	gcc $(OPTS) wallmain.o map.o clutil.o util.o -o waller
	
fromfile:	fromfile.c map.h types.h util.h clutil.h map.o util.o clutil.o
	gcc $(OPTS) fromfile.c map.o util.o clutil.o -o fromfile



wallmain.o: wallmain.c map.h types.h util.h clutil.h
	gcc $(OPTS) wallmain.c -c

	
clutil.o:	clutil.h clutil.c
	gcc $(OPTS) -c clutil.c
	
	
util.o:	util.c util.h types.h
	gcc $(OPTS) -c util.c
	
main.o: main.c errs.h sim.h agent.h types.h map.h
	gcc $(OPTS) -c main.c
	
sim.o:	sim.c sim.h agent.h types.h map.h util.h errs.h
	gcc $(OPTS) -c sim.c
	
map.o: map.c errs.h map.h types.h
	gcc $(OPTS) -c map.c

agent.o: agent.c agent.h types.h util.h
	gcc $(OPTS) -c agent.c

errs.o:	errs.h	errs.c
	gcc $(OPTS) -c errs.c
	
clean:	
	rm -f $(OBJS) $(PROGS)