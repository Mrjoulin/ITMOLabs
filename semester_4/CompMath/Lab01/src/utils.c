#include "../include/utils.h"


_Noreturn void err( const char* msg, ... ) {
    va_list args;
    va_start(args, msg);
    vfprintf(stderr, msg, args); // NOLINT
    va_end(args);
    abort();
}

void print_matrix_with_biases(const uint8_t size, double** matrix, double* biases) {
    printf("\n"); // indents

    for (uint8_t i = 0; i < size; i++) {
        printf("| ");
        for (uint8_t j = 0; j < size; j++) {
            printf("%g ", matrix[i][j]);
        }

        printf("| %c %g\n", i == size / 2 ? '=' : ' ', biases[i]);
    }
    
    printf("\n");
}

void print_vector(const uint8_t size, double* arr) {
    printf("{");

    for (uint8_t i = 0; i < size; i++) 
        printf(" x_%d: %g%c", i + 1, arr[i], i < size - 1 ? ',' : ' ');

    printf("}\n");
}

double** create_matrix(const uint8_t size) {
    double** matrix = (double**) malloc(size * sizeof(double*));

    for(uint8_t i = 0; i < size; i++) 
        matrix[i] = (double*) malloc(size * sizeof(double));

    return matrix;
}

double* create_biases(const uint8_t size) {
    return (double*) malloc(size * sizeof(double));
}


void free_mem(const uint8_t size, double** matrix, double* biases) {
    for(uint8_t i = 0; i < size; i++) free(matrix[i]);
    free(matrix);
    free(biases);
}
