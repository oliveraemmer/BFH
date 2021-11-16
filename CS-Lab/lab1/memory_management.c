#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <stdint.h>
#include <inttypes.h>
#include "memory_management.h"

bool *freeFrames;
uintptr_t *frames;
uintptr_t *pageTable;
uintptr_t frameToAdd;
int offsetLen;
int offsetMask;
int VPNLen;

int TLBMaxSize;
int TLBWrite = 0;
uintptr_t *TLB;

int memory_init_data(int number_processes,
		     int free_frames_number,
		     int length_VPN_in_bits,
		     int length_PFN_in_bits,
		     int length_offset_in_bits,
		     int tlb_size)                
{
	// initialize physical frame list
	freeFrames = (bool *)calloc(free_frames_number + 1, sizeof(bool));
	for(int i = 0; i < 9; i++){
		freeFrames[i] = true;
	}
	freeFrames[9] == 0;

	// initialize page table
	VPNLen = length_VPN_in_bits;
	int pageTableSize = (int) pow(2, VPNLen);
	pageTable = (uintptr_t *)calloc(pageTableSize + 1, sizeof(uintptr_t));
	for(int i = 0; i <= pageTableSize; i++){
		pageTable[i] == 0;
	}

	// initialize offset length
	offsetLen = length_offset_in_bits;
	offsetMask = (int) pow(2, offsetLen) - 1;

	// initialize the actual physical memory
	int numberOfFrames = (int) pow(2, length_PFN_in_bits);
	frames = (uintptr_t *)calloc(numberOfFrames*offsetMask + 2, sizeof(uintptr_t));
	frames[free_frames_number] = 0;

	// initialize TLB
	TLBMaxSize = tlb_size * 2;
	TLB = (uintptr_t *)calloc(tlb_size * 2 + 1, sizeof(uintptr_t));
	for(int i = 0; i < tlb_size * 2; i++){
		TLB[i] = 0;
	}
	TLB[tlb_size * 2] = 0;
       	
	return 0;
}

// TLB Entries -> TLB[i] = physical address & TLB[i+1] = VPN
int write_TLB(uint64_t VPN, uintptr_t pageTableEntry){
	// remove offset from physical address
	uintptr_t pageTableEntry_shifted = pageTableEntry >> offsetLen;
	pageTableEntry_shifted << offsetLen;
	// Write into TLB
	TLB[TLBWrite] = pageTableEntry_shifted;
	TLB[TLBWrite+1] = VPN;
	if(TLBWrite < 9){
		TLBWrite += 2;
	} else {
		TLBWrite = 0;
	}
	
	printf("TLB: %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR "\n", TLB[0], TLB[1], TLB[2], TLB[3], TLB[4], TLB[5], TLB[6], TLB[7], TLB[8], TLB[9]);
	return 0;
}

uintptr_t check_TLB(uint64_t VPN){
	for(int i = 1; i < TLBMaxSize; i+=2){
		if(TLB[i] != 0){
			if(TLB[i] == VPN){
				printf("TLB: %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR ", %" PRIxPTR "\n", TLB[0], TLB[1], TLB[2], TLB[3], TLB[4], TLB[5], TLB[6], TLB[7], TLB[8], TLB[9]);
				return TLB[i-1];
			}
		}
	}

	return 0;
}


int get_physical_address(uint64_t virtual_address,
			 int process_id,
			 uint64_t* physical_address,
			 int* tlb_hit)
{

	// testing if address is too long
	int maxVPN = (int) pow(2, (VPNLen + offsetLen));
	if(virtual_address > maxVPN - 1){
		return 1;
	}

	// get offset bits
	int VPNMask = (int) pow(2, VPNLen) - 1;
	uint64_t vOffset = virtual_address & offsetMask;
	uint64_t virtual_address_shifted = virtual_address >> offsetLen;
	uint64_t VPN = virtual_address >> offsetLen & VPNMask;
	
	printf("\nvirtual_address = %" PRIx64 " -> VPN = %" PRIx64 " vOffset = %" PRIx64 "\n", virtual_address, VPN, vOffset);

	// check if Entry in TLB
	*tlb_hit = 0;
	uintptr_t TLBCheck = check_TLB(VPN);
	if(TLBCheck != 0){
		*tlb_hit = 1;
		*physical_address = TLBCheck;
		printf("TLB hit\n");
	} else {
		*tlb_hit = 0;

		// get last bit of page entry of VPN
		int pageState = pageTable[VPN] & 1;
		bool freeFrameFound = false;
		// if pageState is 0 then the page isn't used yet
		if(pageState == 0 && *tlb_hit != 1){
			// check for freeFrames
			for(int i = 0; i < 9; i++){
				if(freeFrames[i] == true){
					// if a free frame is found, set to false 				
					freeFrameFound = true;
					freeFrames[i] = false;
					printf("Frame %d is now in use\n", i);
					// remove offset from address and add frame to pageTable
					frameToAdd = (uintptr_t) &frames[i*offsetMask+1];
					frameToAdd >> offsetLen;
					frameToAdd << offsetLen;
					pageTable[VPN] = frameToAdd;

					//add frame to TLB
					write_TLB(VPN, pageTable[VPN]);
					break;
				}
				printf("freeFrames[%d] = %d (used)\n", i, freeFrames[i]);
			}
		} else {
			// if pageState is 1 then the page is in use and can be added to the TLB
			write_TLB(VPN, pageTable[VPN]);
		}
		if(freeFrameFound == false && *tlb_hit != 1){
			return 1;
		}
	}
	
	*physical_address = (uint64_t) pageTable[VPN] | vOffset;
	printf("physical_address = %" PRIx64 "\n\n", *physical_address);

	return 0;
}

