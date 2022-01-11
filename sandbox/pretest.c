//----------------------------

﻿#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>


long long int global_counter;

int loops;

void *doloop(void *arg) {
  (void)arg; // suppress compiler warning

  for (int i = 0; i < loops; i++){
    global_counter += 1;
  }

  return NULL;
}

int main(int argc, char** argv) {
  if(3 != argc){
    perror("Wrong number of arguments");
    return EXIT_FAILURE;

  }
  int nb_threads;
  char tmp;
  /* Reads the number of threads */
  int matches = sscanf(argv[1],"%d%c",&nb_threads,&tmp);
  if(1!=matches){
    puts("Argument 1 is not a number");
    return EXIT_FAILURE;

  }
  /* reads the number of loops */
  matches = sscanf(argv[2],"%d%c",&loops,&tmp);
  if(1!=matches){
    puts("Argument 2 is not a number");
    return EXIT_FAILURE;

  }

  pthread_t threads[nb_threads];

  for (int i = 0; i < nb_threads; i++) {
    int err = pthread_create(&(threads[i]), NULL, &doloop, NULL);
    if (err != 0)
      printf("Can't create thread #%s\n", strerror(err));
    else
      printf("Thread created successfully\n");
  }
  for(int i =0; i< nb_threads;i++){
    pthread_join(threads[i],NULL);
  }
  printf("Result = %lld\n",global_counter);
 
  return 0;
}
