SECTION .data           ; Section containing initialised data
    Base64Table:	db "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
    Base64String	db "    "
    Base64StringLen	equ $-Base64String
    Last2		db "    "
    Last2Len		equ $-Last2
    Last1		db "    "
    Last1Len		equ $-Last1

SECTION .bss            ; Section containing uninitialized data
    InBufLen: 		equ 3
    InBuf:		resb InBufLen
SECTION .text           ; Section containing code

global _start           ; Linker needs this to find the entry point!

_start:

read:

	mov rax, 0
	mov rdi, 0
	mov rsi, InBuf
	mov rdx, InBufLen
	syscall

	cmp rax, 0
	je exit

	cmp rax, 3
	je process

	cmp rax, 2
	je processLast2

	cmp rax, 1
	je processLast1

write:
	mov rax, 1
	mov rdi, 1
	mov rsi, Base64String
	mov rdx, Base64StringLen
	syscall

	jmp read

process:

	; Bytes in Register einfügen
	xor rax, rax
	xor rbx, rbx
	xor rdx, rdx
	mov al, byte [InBuf]
	mov bl, byte [InBuf+1]
	mov dl, byte [InBuf+2]
	
	; Erstes Base64 Zeichen
	push rax
	and al, 0xfc
	shr al, 2
	mov al, byte [Base64Table+rax]
	mov byte [Base64String], al
	pop rax
	
	; Zweites
	and al, 0x03
	shl al, 4
	push rbx
	and bl, 0xf0
	shr bl, 4
	or al, bl
	mov al, byte [Base64Table+rax]
	mov byte [Base64String+1], al
	pop rbx

	; Drittes
	and bl, 0x0f
	shl bl, 2
	push rdx
	and dl, 0xc0
	shr dl, 6
	or bl, dl
	mov bl, byte [Base64Table+rbx]
	mov byte [Base64String+2], bl
	pop rdx

	; Viertes
	and dl, 0x3f
	mov dl, byte [Base64Table+rdx]
	mov byte [Base64String+3], dl
	
	jmp write

processLast2:

	; Gleich wie bei process aber nur 3 Base64 Zeichen
	xor rax, rax
	xor rbx, rbx
	mov al, byte [InBuf]
	mov bl, byte [InBuf+1]

	push rax
	and al, 0xfc
	shr al, 2
	mov al, byte [Base64Table+rax]
	mov byte [Last2], al
	pop rax

	and al, 0x03
	shl al, 4
	push rbx
	and bl, 0xf0
	shr bl, 4
	or al, bl
	mov al, byte [Base64Table+rax]
	mov byte [Last2+1], al
	pop rbx
	
	and bl, 0x0f
	shl bl, 2
	mov bl, byte [Base64Table+rbx]
	mov byte [Last2+2], bl
	
	mov bl, byte [Base64Table+64]
	mov byte [Last2+3], bl

	mov rax, 1
	mov rdi, 1
	mov rsi, Last2
	mov rdx, Last2Len
	syscall

	jmp read

processLast1:
	
	; Gleich wie process aber nur zwei Base64 Zeichen
	xor rax, rax
	mov al, byte [InBuf]

	push rax
	and al, 0xfc
	shr al, 2
	mov al, byte [Base64Table+rax]
	mov byte [Last1], al
	pop rax

	and al, 0x03
	shl al, 4
	mov al, byte [Base64Table+rax]
	mov byte [Last1+1], al

	mov al, byte [Base64Table+64]
	mov byte [Last1+2], al

	mov al, byte [Base64Table+64]
	mov byte [Last1+3], al

	mov rax, 1
	mov rdi, 1
	mov rsi, Last1
	mov rdx, Last1Len
	syscall

	jmp read

exit:	

    mov rax, 60         ; Code for exit
    mov rdi, 0          ; Return a code of zero
    syscall             ; Make kernel call
