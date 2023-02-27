//
// Created by Matthew Ivanov on 06.02.2023.
//

#include "../include/input.h"
#include "../include/solve.h"


int main(int argc, char** argv) {
    bool from_file = read_from_file();

    uint8_t size = input_matrix_size(from_file);

    // Alloc matrix and biases

    double** matrix = create_matrix(size);
    double* biases = create_biases(size);

    input_matrix(size, matrix, from_file);
    input_biases(size, biases, from_file);
    
    // Check matrix coorect, make it diagonal
    make_diagonal(size, matrix, biases);
    
    // Print matrix with biases
    printf("\nGiven matrix (diagonalised):\n");
    print_matrix_with_biases(size, matrix, biases);
    
    // Input precision to compute answer
    double eps = input_precision(from_file);

    // Solve system
    solve_system(size, matrix, biases, eps);

    // Free memory
    free_mem(size, matrix, biases);

    return 0;
}