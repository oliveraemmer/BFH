#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <stdint.h>
#include <sys/types.h>
#include <sys/stat.h>

/*int sort(int counter, char **fileNames) {
	char temp1[101] = {0};
	char temp2[101] = {0};
	bool notSorted = true;
	while(notSorted){
		notSorted = false;	
		for (int i = 0; i < counter-1; i++) {
			for (int y = 0; y < 101; y++){
				strncpy(temp1, fileNames[i], 101);
				strncpy(temp2, fileNames[i+1], 101);
				// make lowercase
				for (long unsigned int j = 0; j < sizeof(temp1); j++) {
					temp1[j] = tolower(temp1[j]);
				}
				for (long unsigned int j = 0; j < sizeof(temp2); j++) {
					temp2[j] = tolower(temp2[j]);
				}
				// sort
				if(temp1[y] > temp2[y]){
					printf("%d is bigger than %d%s", temp1[y], temp2[y], "\n");
					if (temp2[y] == 0) {
						break;
					}
					notSorted = true;
					strncpy(temp1, fileNames[i], 101);
					strncpy(temp2, fileNames[i+1], 101);
					memset(fileNames[i], 0, 100);
					memset(fileNames[i+1], 0, 100);
					strncpy(fileNames[i], temp2, 101);
					strncpy(fileNames[i+1], temp1, 101);
					break;
				} else if (temp1[y] == temp2[y]){
					continue;
				}else{
					break;
				}
			}
		}
	}
	for (int i = 0; i < counter; i++) {
		for (int y = 0; y < 101; y++) {
			if (fileNames[i][y] == 0){
				printf("%s", "\n");
				break;
			}
			
			printf("%c", fileNames[i][y]);
		}
		
	}
	return 0;
}*/

int ls(char* path) {
	struct dirent **dir;
	// scan the directory
	int count = scandir(path, &dir, NULL, alphasort);
	//printf("%d", count);
	// check if path points to symlink
	struct stat s;
	bool sym;
	if (lstat(path, &s) == 0 && S_ISLNK(s.st_mode) == 1) {
		sym = true;
	} else {
		sym = false;
	}
	// scandir fails when path points to a single file
	if(count == -1){
		if (sym) {
			printf("%s%s\n", path, "@");
		} else if (access(path, F_OK) == 0 && access(path, X_OK) != 0) {
			printf("%s\n", path);
		} else if (access(path, F_OK) == 0 && access(path, X_OK) == 0) {
			printf("%s%s\n", path, "*");
		}
		return 0;		
	}

	// execute this if scandir doesn't fail
	for (int i = 0; i < count; i++){

		
		// filter hidden files
		if (strncmp(dir[i]->d_name, ".", 1) == 0) {
			continue;
		}

		// print the files/folders
		if(dir[i]->d_type == DT_DIR) {
			printf("%s%s\n", dir[i]->d_name, "/");
		} else if (dir[i]->d_type == DT_REG) {
			// check if executable
			char *filePath = malloc(strlen(dir[i]->d_name) + strlen(path) + 1);
			strcpy(filePath, path);
			strcpy(filePath, dir[i]->d_name);
			if(access(filePath, X_OK) == 0) {
				printf("%s%s\n", dir[i]->d_name, "*");
			} else {
				printf("%s\n", dir[i]->d_name);
			}
			
		}else if (dir[i]->d_type == DT_LNK) {
			printf("%s%s\n", path, "@");
		}
	}
	return 0;
}





int main(int argc, char **argv) {
	//printf("%d", argc);
	
	if(argc == 2) {
		ls(argv[1]);
	} else if (argc == 1) {
		argv[1] = ".";
		ls(argv[1]);
	}
	return 0;
}
