#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int sort(bool asc) {
	char **sortArray = (char **)calloc(20, sizeof(char*));
	if (NULL == sortArray) {return EXIT_FAILURE;}
	sortArray[0] = (char *)calloc(101, sizeof(char));
	if (NULL == sortArray) {return EXIT_FAILURE;}

	int counter = 0;
	char input[101] = {0};
	while (1 == scanf("%s", input)) {
		strncpy(sortArray[counter], input, 101);
		memset(input, 0, sizeof input);
		counter++;
		sortArray[counter] = (char * )calloc(101, sizeof(char));
	}
	
	char temp1[101] = {0};
	char temp2[101] = {0};
	bool notSorted = true;
	// let it loop until it goes through without having to sort
	while(notSorted) {
		notSorted = false;	
		for (int i = 0; i < counter-1; i++) {
			for (int y = 0; y < 101; y++) {
				strncpy(temp1, sortArray[i], 101);
				strncpy(temp2, sortArray[i+1], 101);
				// make lowercase
				for (long unsigned int j = 0; j < sizeof(temp1); j++) {
					temp1[j] = tolower(temp1[j]);
				}
				for (long unsigned int j = 0; j < sizeof(temp2); j++) {
					temp2[j] = tolower(temp2[j]);
				}
				// sort
				if(temp1[y] > temp2[y]){
					if (temp2[y] == 0) {
						break;
					}
					notSorted = true;
					strncpy(temp1, sortArray[i], 101);
					strncpy(temp2, sortArray[i+1], 101);
					memset(sortArray[i], 0, 100);
					memset(sortArray[i+1], 0, 100);
					strncpy(sortArray[i], temp2, 101);
					strncpy(sortArray[i+1], temp1, 101);
					break;
				} else if (temp1[y] == temp2[y]){
					continue;
				}else{
					break;
				}
			}
		}
	}

	// print
	if(asc) {
		for (int i = 0; i < counter; i++) {
			for (int y = 0; y < 101; y++) {
				if (sortArray[i][y] == 0){
					break;
				}
				printf("%c", sortArray[i][y]);
			}
			printf("%s", "\n");
		}
	} else {
		for (int i = counter-1; i >= 0; i--){
			for (int y = 0; y < 101; y++){
				if (sortArray[i][y] == 0){
					break;
				}
				printf("%c", sortArray[i][y]);
			}
			printf("%s", "\n");
		}	
	}
	return 0;
}

int main(int argc, char **argv) {
	bool asc = true;
	if(argc == 2 && strcmp(argv[1], "r")) {
		asc = false;
	}
	
	sort(asc);
	return 0;
}

