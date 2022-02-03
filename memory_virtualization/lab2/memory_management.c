#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <stdint.h>
#include <inttypes.h>
#include <pthread.h>
#include "memory_management.h"

int globalCounter = 0;

bool *freeFrames;
uintptr_t *frames;
uintptr_t *pageTable;
uintptr_t frameToAdd;
int freeFramesNumber;
int offsetLen;
int offsetMask;
int VPNLen;
int numProc;
int TLBMaxSize;
int maxVPN;
int VPNMask;
uintptr_t *TLB;
int TLBWrite;

pthread_mutex_t lock;

struct process {
	int p_id;
	uintptr_t *pageTable;
};

typedef struct process process;

process *processes;

int memory_free_data(){
	for(int i = 0; i < numProc; i++){
		free(processes[i].pageTable);
	}
	free(TLB);
	free(processes);
	free(freeFrames);
	free(frames);
	//free(processes[n].TLB);
}

int memory_init_data(int number_processes,
		     int free_frames_number,
		     int length_VPN_in_bits,
		     int length_PFN_in_bits,
		     int length_offset_in_bits,
		     int tlb_size)                
{

	// initialize list of processes
	numProc = number_processes;
	processes = (process *)calloc(numProc + 1, sizeof(process));
	for(int i = 0; i < numProc; i++){
		process p;
		p.p_id = -1;
		processes[i] = p;
	}

	// initialize physical frame list
	freeFramesNumber = free_frames_number;
	freeFrames = (bool *)calloc(freeFramesNumber + 1, sizeof(bool));
	for(int i = 0; i < freeFramesNumber; i++){
		freeFrames[i] = true;
	}

	// initialize page table
	VPNLen = length_VPN_in_bits;

	// initialize offset length
	offsetLen = length_offset_in_bits;
	offsetMask = (int) pow(2, offsetLen) - 1;

	// initialize VPN Mask
	VPNMask = (int) pow(2, VPNLen) - 1;


	// max. VPN length
	maxVPN = (int) pow(2, (VPNLen + offsetLen));


	// initialize the actual physical memory
	int numberOfFrames = (int) pow(2, length_PFN_in_bits);
	frames = (uintptr_t *)calloc(numberOfFrames*offsetMask + 2, sizeof(uintptr_t));

	// initialize TLB
	TLBMaxSize = tlb_size * 3; 
	TLB = (uintptr_t *)calloc(TLBMaxSize + 1, sizeof(uintptr_t));
	for(int i = 0; i < TLBMaxSize; i++){
		TLB[i] = 0;
	}
	return 0;
}

// TLB Entries -> TLB[i] = physical address & TLB[i+1] = VPN
int write_TLB(uint64_t VPN, uintptr_t pageTableEntry, process *current_p){
	// remove offset from physical address
	uintptr_t pageTableEntry_shifted = pageTableEntry >> offsetLen;
	pageTableEntry_shifted << offsetLen;
	// Write into TLB
	printf("pagetableEntry_shifted %"PRIxPTR"\n", pageTableEntry_shifted);
	printf("TLBWrite = %d\n", TLBWrite);
	TLB[TLBWrite] = pageTableEntry_shifted;
	TLB[TLBWrite+1] = VPN;
	TLB[TLBWrite+2] = current_p->p_id;
	if(TLBWrite < (TLBMaxSize-3)){
		TLBWrite += 3;
	} else {
		TLBWrite = 0;
	}
		
	

	
	printf("TLB: %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR "\n", TLB[0], TLB[1], TLB[2], TLB[3], TLB[4], TLB[5], TLB[6], TLB[7], TLB[8], TLB[9], TLB[10], TLB[11], TLB[12], TLB[13], TLB[14]);
	return 0;
}

uintptr_t check_TLB(uint64_t VPN, process *current_p){
	for(int i = 1; i < TLBMaxSize; i+=3){
		printf("Checking: i = %d\n", i);
		//printf("TLB[i] = %"PRIxPTR"\n", TLB[i]);
		if(TLB[i] != 0 && TLB[i] == VPN && current_p->p_id == TLB[i+1]){
			printf("TLB: %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR "\n", TLB[0], TLB[1], TLB[2], TLB[3], TLB[4], TLB[5], TLB[6], TLB[7], TLB[8], TLB[9], TLB[10], TLB[11], TLB[12], TLB[13], TLB[14]);
			return TLB[i-1];	
		}
	}

	return 0;
}







int get_physical_address(uint64_t virtual_address,
			 int process_id,
			 uint64_t* physical_address,
			 int* tlb_hit)
{

	int p_id = process_id;
	// current process in list of processes[]
	int current_p;
	bool processFound = false;
	printf("\n\np_id = %" PRIx64"\n", p_id);
	
	// find process in processes[]
	for(int i = 0; i < numProc; i++){
		if(processes[i].p_id == p_id){
			//printf("Process found in processes[%d]\n", i);
			processFound = true;
			current_p = i;
			break;
		}
	}

	// if the current process is not in processes[]
	if(!processFound){
		// find next empty spot in processes[] (-1)
		for(int i = 0; i < numProc; i++){
			pthread_mutex_lock(&lock);
				if(processes[i].p_id == -1){
						//printf("New process in processes[%d]\n", i);
						current_p = i;
						processes[i].p_id = p_id;
						pthread_mutex_unlock(&lock);
						break;
				}
			pthread_mutex_unlock(&lock);
		}

		// initialize pagetable
		int pageTableSize = (int) pow(2, VPNLen);
		pageTable = (uintptr_t *)calloc(pageTableSize + 1, sizeof(uintptr_t));
		for(int i = 0; i <= pageTableSize; i++){
			pageTable[i] == 0;
		}
		pageTable[pageTableSize] = 0;
		
		processes[current_p].pageTable = pageTable;
	}

	// testing if address is too long
	if(virtual_address > maxVPN - 1){
		return 1;
	}

	// get offset bits
	uint64_t vOffset = virtual_address & offsetMask;
	uint64_t virtual_address_shifted = virtual_address >> offsetLen;
	uint64_t VPN = virtual_address >> offsetLen & VPNMask;
	
	printf("virtual_address = %" PRIx64 " -> VPN = %" PRIx64 " vOffset = %" PRIx64 "\n", virtual_address, VPN, vOffset);

	// check if Entry in TLB
	*tlb_hit = 0;
	pthread_mutex_lock(&lock);
		uintptr_t TLBCheck = check_TLB(VPN, &processes[current_p]);
	pthread_mutex_unlock(&lock);
	if(TLBCheck != 0){
		*tlb_hit = 1;
		*physical_address = TLBCheck;
		printf("TLB hit\n");
	} else {
		*tlb_hit = 0;

		// get last bit of page entry of VPN
		int pageState = processes[current_p].pageTable[VPN] & 1;
		bool freeFrameFound = false;
		// if pageState is 0 then the page isn't used yet
		if(pageState == 0 && *tlb_hit != 1){
			// check for freeFrames
			for(int i = 0; i < freeFramesNumber; i++){
				if(freeFrames[i] == true){
					// if a free frame is found, set to false 				
					freeFrameFound = true;
					freeFrames[i] = false;
					printf("Frame %d is now in use\n", i);
					// remove offset from address and add frame to pageTable
					frameToAdd = (uintptr_t) &frames[i*offsetMask+1];
					frameToAdd = frameToAdd >> offsetLen;
					frameToAdd = frameToAdd << offsetLen;
					processes[current_p].pageTable[VPN] = frameToAdd;

					//add frame to TLB
					write_TLB(VPN, processes[current_p].pageTable[VPN], &processes[current_p]);
					break;
				}
				//printf("freeFrames[%d] = %d (used)\n", i, freeFrames[i]);
			}
		} else {
			// if pageState is 1 then the page is in use and can be added to the TLB
			write_TLB(VPN, processes[current_p].pageTable[VPN], &processes[current_p]);
		}
		if(freeFrameFound == false && *tlb_hit != 1){
			return 1;
		}
	}
	
	*physical_address = (uint64_t) processes[current_p].pageTable[VPN] | vOffset;
	//printf("Check if TLB entry was made: processes[current_p].TLB[1] = %"PRIxPTR"\n", processes[current_p].TLB[1]);

	return 0;
}

