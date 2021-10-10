#include <stdio.h>
#include <time.h>
#include <stdint.h>
#include <stdlib.h>
#include <stdbool.h>
#include <unistd.h>

int main(void) {
	struct timespec ts;

	if (clock_gettime(CLOCK_REALTIME, &ts) == -1){
		perror("clocktime");
		exit(EXIT_FAILURE);
	}
	long start = ts.tv_nsec;
	
	int i = 1;
	while (i > 0){
		printf("Hello World\n");
		i--;
	}

	if (clock_gettime(CLOCK_REALTIME, &ts) == -1){
		perror("clocktime");
		exit(EXIT_FAILURE);
	}
	long end = ts.tv_nsec;
	long time = end - start;
	printf("Time: %ldns, Start; %ldns, End: %ldns\n", time, start, end);

}
