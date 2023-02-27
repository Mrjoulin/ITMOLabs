#ifndef INPUT_FILE
#define INPUT_FILE

#include <stdbool.h>

#include "utils.h"

#define MAX_MATRIX_SIZE 20

bool read_from_file();

uint8_t input_matrix_size(bool from_file);

double input_precision(bool from_file);

void input_matrix(const uint8_t size, double** matrix, bool from_file);

void input_biases(const uint8_t size, double* biases, bool from_file);

#endif