#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <stdbool.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

extern int errno;

int getVar(char *input){
	char dollar[2] = "$";
	char bracketR[2] = "}";
	char bracketL[2] = "{";
	char space[2] = " ";
	char varName[64] = {0};
	char var[64] = {0};
	char temp[64] = {0};
	char temp2[64] = {0};
	//printf("input = \"%s\"\n", input);
	fflush(stdout);
	// change variable to actual value
	if (strchr(input, dollar[0]) != 0) {
		int done = 0;
		while(!done){
		memset(temp, 0, 64);
		memset(temp2, 0, 64);
		done = 1;
		int j = 0;
		int iTemp = 0;
		int iTemp2 = 0;
		for(int i = 0; i < 64; i++) {
			// if a dollar sign is found, get the variable name
			if(input[i] == dollar[0]) {
				int ii = 0;
				// if the next character is a left bracket
				if(input[i+1] == bracketL[0]) {
					ii = 2;
				} else {

					ii = 1;
				}
				// get variable name
				while(input[i+ii] != bracketR[0] && input[i+ii] != 0 && input[i+ii] != dollar[0] && input[i+ii] != space[0]){
					varName[j] = input[i+ii];
					ii++;
					j++;
				}
				//printf("varName = %s\n", varName);
				fflush(stdout);
				// copy content into var
				if(getenv(varName) != NULL) {
					strcpy(var, getenv(varName));
				}
				//printf("var = %s\n", var);
				fflush(stdout);

				// if there's more after the variable, add the content to temp 
				while(input[i+ii] != 0) {
					// check if bracket belongs to this var or to the next
					// only remove it if it belongs to this var
					int test = ii - j;
					if(input[i+ii] == bracketR[0] && test < 3) {
						ii++;
						continue;
					}
					temp[iTemp] = input[i+ii];
					iTemp++;
					ii++;
				}
				//printf("temp = %s\n", temp);
				//printf("temp2 = %s\n", temp2);
				fflush(stdout);

				
				// Replace parts[1] with var
				strcat(temp2, var);
				strcat(temp2, temp);
				strncpy(input, temp2, 64);
				//memset(temp, 0, 64);
				//memset(temp2, 0, 64);
				iTemp = 0;
				iTemp2 = 0;
				done = 0;
				break;	
			} else {
				temp2[iTemp2] = input[i];
				iTemp2++;
			}
		}
		}
	} else {
		//printf("\nnot var");
		fflush(stdout);
	}
	return 0;
}
int execute(char **parts) {
	if (strcmp(parts[0], "expr") == 0) {
		if (execvp(parts[0], parts) == -1) {
			return 1;
		}	
	} else if (strcmp(parts[0], "echo") == 0) {
		if(execvp(parts[0], parts) == -1) {
			return 1;
		}
	}
	if(execvp(parts[0], parts) == -1) {
		return 1;
	}
	return 0;
}

int setVar(char *input, char **parts) {
	// same as split() but with "="

	parts = (char **)calloc(1, sizeof(char*));
	if (NULL == parts) {exit(EXIT_FAILURE);}
	parts[0] = (char *)calloc(64, sizeof(char));
	if (NULL == parts) {exit(EXIT_FAILURE);}
	char *token;
	//printf ("setenv\n");

	char delim[2] = "=";
	token = strtok(input, delim);
	int counter = 0;
	while (token != NULL) {
		parts[counter] = token;
		//printf("%s\n", parts[counter]);
		counter++;
		parts = (char **)realloc(parts, counter + 1 * sizeof(char*));
		if (NULL == parts) {exit(EXIT_FAILURE);}
		parts[counter] = (char *)calloc(64, sizeof(char));
		if(NULL == parts) {exit(EXIT_FAILURE);}
		token = strtok(NULL, delim);
	}
	parts[counter] = NULL;
	if(getenv(parts[0]) == NULL){
		//printf("varname is available\n");
		setenv(parts[0], parts[1], 0);
	} else {
		//printf("varname is used\n");
		getVar(parts[1]);
		setenv(parts[0], parts[1], 1);	
	}
	char test[64] = {0};
	strcpy(test, getenv(parts[0]));
	//printf("Test if var got set: %s\n", test);


	return 0;
}

int getInput(char *input) {
	printf("$ ");
	fflush(stdout);
	char c;
	while(1) {
		c = fgetc(stdin);
		if(c == '\n') {
			return 0;
		} else if (c == EOF) {
			return 1;
		}
		strncat(input, &c, 1);


	}
	return 0;
}

char **split(char *input, char **parts) {
	parts = (char **)calloc(1, sizeof(char*));
	if (NULL == parts) {exit(EXIT_FAILURE);}
	parts[0] = (char *)calloc(64, sizeof(char));
	if (NULL == parts) {exit(EXIT_FAILURE);}
	char *token;
	//printf ("Splitting...\n");

	char delim[2] = " ";
	token = strtok(input, delim);
	int counter = 0;
	while (token != NULL) {
		parts[counter] = token;
		//printf("%s", parts[counter]);
		counter++;
		parts = (char **)realloc(parts, (counter + 1) * sizeof(char*));
		if (NULL == parts) {perror("Error: ");exit(EXIT_FAILURE);}
		parts[counter] = (char *)calloc(64, sizeof(char));
		if(NULL == parts) {perror("Error: ");exit(EXIT_FAILURE);}
		token = strtok(NULL, delim);
	}
	parts[counter] = NULL;
	return parts;
}

int main(int argc, char **argv) {
	while(1){
		setenv("?", "0", 1);
		//char longInput[64] = {0};	// raw input
		char *input = (char *)calloc(64, sizeof(char));	// input with 0s cut off
		int counter = 0;	// number of letters entered
		char **splitParts;	// multi-array of the parts separated by spaces
		int splitCounter = 0;	// number of parts
		char equals[2] = "=";
		char and[2] = "&";
		char **parts = NULL;
		bool backgr = false;
		char redirOut[2] = ">";
		char redirIn[2] = "<";
		char space[2] = " ";
		char file[32] = {0};
		char command[32] = {0};
		bool redirInBool = false;
		bool redirOutBool = false;
		char cat[6] = "cat -";

		// Get input from user
		if (getInput(input) == 1) {
			break;
		}
		if (input[0] == 0) {
			setenv("?", "0", 1);
		}
		//printf("\nGot input from getInput():\n");
		for (int i = 0; input[i] != 0; i++) {
			//printf("%c", input[i]);
			counter++;
		}
		//printf("\ncounter = %d \n", counter);

		// Check if Background
		if (strchr(input, and[0]) != 0) {
			//printf("\nBackground");
			backgr = true;
		}
		
		// Redirect
		if(strchr(input, redirIn[0]) != 0 || strchr(input, redirOut[0]) != 0) {
			// remove spaces
			for (int i = 0; i < counter; i++) {
				if (input[i] == space[0] && (input[i+1] == redirIn[0] || input[i+1] == redirOut[0] || input[i-1] == redirIn[0] || input[i-1] == redirOut[0])){
					//printf("Space\n");
					counter--;
					int ii = i;
					for (int j = i; j < counter; j++) {
						input[j] = input[ii+1];
						//printf("input[j] = %c\n", input[j]);
						ii++;
					}
					input[ii] = 0;
					//printf("input = %s\n", input);
				}
			}
			bool found = false;
			for (int i = 0; i < counter; i++) {
				if (input[i] == redirIn[0] || input[i] == redirOut[0]) {
					found = true;
					int ii = 0;
					// get filename
					if (input[i+1] == redirIn[0] || input[i+1] == redirOut[0]) {
						i++;
					}
					for (int j = i+1; j < counter; j++) {
						file[ii] = input[j];
						ii++;
					}
					// check if in our out
					if (input[i] == redirIn[0]) {
						//printf("redirInBool\n");
						redirInBool = true;
					}
					if (input[i] == redirOut[0]) {
						//printf("redirOutBool\n");
						redirOutBool = true;
					}
				// get command
				} else if (!found) {
					command[i] = input[i];
				}
			}
			//printf("current input: %s\n", input);
			//printf("filename: %s\n", file);
			//printf("command: %s\n", command);
			if (strstr(command, cat)) {
				memset(command, 0, 32);
				strcpy(command, "cat");
			}
		}

		// Check if there's an equal in the first statement
		if (strchr(input, equals[0]) != 0) {
			char sInput[64] = {0};
			for (int i = 0; i < counter; i++) {
				sInput[i] = input[i];
			}
			if(setVar(sInput, parts) == 0) {
				//printf("Variables set\n");
			}
			// Empty array for next round
			for (int i = 0; i < counter; i++){
				input[i] = 0;
			}
			counter = 0;
			continue;
		}
		//printf("Current Input = %s\n", input);

		// Change all variables to their actual value
		//printf("Changing variables to values\n");
		if(getVar(input) == 0){
			//printf("Variables changed\n");
			//printf("Current Input = %s\n", input);
		}

		// Split the input and count how many parts there are
		// Check if input has spaces or if it's a variable declaration
		if (redirInBool || redirOutBool) {
			splitParts = split(command, parts);
		} else {
			splitParts = split(input, parts);
		}

		//printf("\nGot splits from split():\n");
		while(1) {
			if(splitParts[splitCounter] == 0) {
				break;
			} else {
				splitCounter++;
			}
		}

		if (redirInBool) {
			splitParts = (char **)realloc(splitParts, (splitCounter + 1) * sizeof(char*));
			splitParts[splitCounter] = (char*)calloc(64, sizeof(char));
			splitParts[splitCounter] = file;
			splitCounter++;
		}
		//printf("Split parts: %d\n", splitCounter);
		for (int i = 0; i < splitCounter; i++) {
			//printf("splitParts[%d] = %s\n", i, splitParts[i]);
		}



	//----------execute------------------------------------------------
		fflush(stdout);
		pid_t pid = fork();
		//printf("\nPID: %d\n", pid);
		if(pid < 0) {
			perror("Error: ");
		} else if (0 == pid) {
			if (redirInBool) {
			}
			if (redirOutBool) {
			}
			int openFile = open(&file[0], O_RDWR | O_CREAT, 0666);
			if (file < 0) {
				perror("Error: ");
			}
			if (redirInBool) {
				//printf("redirInBool\n");
				fflush(stdout);
				close(STDIN_FILENO);
				dup2(openFile, STDIN_FILENO);
				close(openFile);
			}
			if (redirOutBool) {
				//printf("redirOutBool\n");
				fflush(stdout);
				close(STDOUT_FILENO);
				dup2(openFile, STDOUT_FILENO);
				close(openFile);
			}
			//printf("executing\n");
			fflush(stdout);
			if (execute(splitParts) == 1) {
				return 1;
			}
		} else if (pid > 0 && !backgr) {
			int status;
			waitpid(pid, &status, 0);

			if ( 0 != WEXITSTATUS(status)) {
				setenv("?", "1", 1);
			} else { 
				setenv("?", "0", 1);
			}
		} else if (pid > 0 && backgr) {
			setpgid(pid,0);
			continue;
		}
		
		// Empty arrays for next round
		for (int i = 0; i < counter; i++)
			input[i] = 0;
		for (int i = 0; i < splitCounter; i++)
			splitParts[i] = 0;

		counter = 0;
		splitCounter = 0;
		//printf("\nCurrent input at end: %s\n", input);
		//free(input);		
		free(splitParts);
	}
	return 0;
}
