#ifndef UTILS_FILE
#define UTILS_FILE

#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <stddef.h>
#include <stdint.h>
#include <inttypes.h>

_Noreturn void err( const char* msg, ... );

void print_matrix_with_biases(const uint8_t size, double** matrix, double* biases);

void print_vector(const uint8_t size, double* arr);

double** create_matrix(const uint8_t size);
double* create_biases(const uint8_t size);

void free_mem(const uint8_t size, double** matrix, double* biases);

#endif