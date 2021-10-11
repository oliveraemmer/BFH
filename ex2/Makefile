all: ex2 fork

ex2: ex2.o
	gcc -o ex2 ex2.o

fork: fork.o
	gcc -o fork fork.o

ex2.o: ex2.c
	gcc -O -Wall -c ex2.c

fork.o: fork.c
	gcc -O -Wall -c fork.c

clean:
	rm -f ex2.o fork.o
