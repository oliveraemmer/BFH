#include <stdio.h>
#include <unistd.h>

int main (void) {
	fork();
	fork();
	fork();
	printf("%d\n", 1);
	return 0;
}
