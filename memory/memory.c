#include <stdio.h>
#include <stdlib.h>

int method(int *p1) {

	int v = 5;
	printf("\nIn Method\nv = %p\n", &v);

	static int s = 8;
	printf("s = %p\n", &s);

	printf("*p1 = %p\n", p1);

	int *p2 = malloc(2);
	printf("*p2 = %p\n", p2);

	return 0;
}

int main(void) {

	int a = 6;
	printf("a = %p\n", &a);
	
	static int b = 7;
	printf("b = %p\n", &b);
	
	int *pc = malloc(2);
	printf("*pc = %p\n", pc);

	const char *str = "string";
	printf("*str = %p\n", str);
	
	method(pc);
	
	return 0;
}
