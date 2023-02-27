#ifndef SOLVE_FILE
#define SOLVE_FILE

#include <math.h>
#include "utils.h"

#define MAX_OPERATIONS_NUMBER 10000

void make_diagonal(const uint8_t size, double** matrix, double* biases);

void solve_system(const uint8_t size, double** matrix, double* biases, double eps);

#endif