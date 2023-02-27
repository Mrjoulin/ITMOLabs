#include "../include/input.h"

bool read_from_file() {
    printf("Enter filename to read from or leave blank to enter manually: ");

    char line[100];
    ssize_t read = scanf("%100[^\n]%*c", line);

    if (read > 0) {
        if (freopen(line, "r", stdin) == NULL)
            err("Unable to open file: %s!", line);
        return true;
    } else if (read == EOF) err("EOF, end execution!\n");

    return false;
}

uint8_t input_matrix_size(bool from_file) {
    uint8_t n;

    do {
        if (!from_file) printf("Enter matrix size (max size %d): ", MAX_MATRIX_SIZE);
        if (scanf("%" SCNu8, &n) <= 0) err("Incorrect input!\n");
    } while (n <= 0 || n > MAX_MATRIX_SIZE);

    return n;
}

double input_precision(bool from_file) {
    double eps;

    do {
        if (!from_file) printf("Enter precision ε (min 1e-6): ");
        if (scanf("%lf", &eps) <= 0) err("Incorrect input!\n");
    } while (eps < 1e-6);

    return eps;
}


void input_matrix(const uint8_t size, double** matrix, bool from_file) {
    if (!from_file) 
        printf("Enter coefs matrix %" PRIu8 "x%" PRIu8 " (A):\n", size, size);

    for (uint8_t i = 0; i < size; i++) {
        for (uint8_t j = 0; j < size; j++) {
            if (scanf("%lf", matrix[i] + j) <= 0) err("Incorrect input!\n");
        }
    }
}

void input_biases(const uint8_t size, double* biases, bool from_file) {
    if (!from_file) 
        printf("Enter biases, %" PRIu8 " values (B):\n", size);
    
    for (uint8_t i = 0; i < size; i++) {
        if (scanf("%lf", biases + i) <= 0) err("Incorrect input!\n");
    }
}