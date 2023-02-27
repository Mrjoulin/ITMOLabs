#include "../include/solve.h"

void apply_permutation(const uint8_t size, double** matrix, double* biases, int8_t* indexes) {
    // Make peremutation
    for (uint8_t i = 0; i < size; i++) {
        uint8_t cur = i;

        while (indexes[cur] >= 0) {
            if (indexes[cur] == i) { // End of cycle
                indexes[cur] = -1;
                break;
            }

            double* temp_mrx = matrix[cur];
            double temp_bias = biases[cur];
            int8_t next = indexes[cur];

            // swap matrix lines
            matrix[cur] = matrix[next];
            matrix[next] = temp_mrx;
            // swap biases
            biases[cur] = biases[next];
            biases[next] = temp_bias;

            indexes[cur] = -1;
            cur = next;
        }
    }
}

void make_diagonal(const uint8_t size, double** matrix, double* biases) {
    int8_t indexes[size]; // permutation indexes (init with -1)
    for (uint8_t i = 0; i < size; i++) indexes[i] = -1;

    uint8_t strict = 0;

    // Check if we can make matrix diagonal
    for (uint8_t i = 0; i < size; i++) {
        double s = 0, max_val = 0;
        uint8_t max_ind = 0, non_zero = 0;

        // Abs sum elements, find max (value and index), count zeros
        for (uint8_t j = 0; j < size; j++) {
            s += fabs(matrix[i][j]);

            if (fabs(matrix[i][j]) > max_val) {
                max_val = fabs(matrix[i][j]);
                max_ind = j;
            }
            if (matrix[i][j] != 0) non_zero++;
        }

        if (non_zero == 0) {
            if (biases[i] == 0) err("Infinite number of system solutions - zero line №%" PRIu8 "\n", i);
            else err("No system solutions - zero line №%" PRIu8 " equals non-zero\n", i);
        }

        // If max in line lower than sum of other values in line
        if (max_val < s - max_val)
            err("Impossible to bring the system to diagonal dominance! Line %d (max < sum)\n", i);

        // if in line only 2 equal numbers (other numbers are zeros), we can take any
        uint8_t other_max_ind = max_ind;
        if (max_val == s - max_val && non_zero == 2)
            for (uint8_t j = max_ind + 1; j < size; j++) if (matrix[i][j]) other_max_ind = j;

        // Set strict falg if max strictly greater then sum
        if (max_val > s - max_val) strict = 1;

        if (indexes[max_ind] < 0) {
            indexes[max_ind] = i;

            if (other_max_ind != max_ind && indexes[other_max_ind] < 0)
                indexes[other_max_ind] = i;
        } else if (indexes[other_max_ind] < 0) {
            indexes[other_max_ind] = i;
        } else {
            // look for same value like indexes[max_ind] or indexes[other_max_ind] in indexes
            uint8_t ind = 0;
            for (; ind < size; ind++) {
                if (ind != max_ind && ind != other_max_ind) {
                    // If we find smth means there is another line in matrix 
                    // with two maxes and one of maxes index same with max_ind or other_max_ind
                    if (indexes[ind] == indexes[max_ind]) {
                        indexes[max_ind] = i;
                        break;
                    }
                    if (indexes[ind] == indexes[other_max_ind]) {
                        indexes[other_max_ind] = i;
                        break;
                    }
                }
            }

            if (ind == size) err("Impossible to bring the system to diagonal dominance! Line %d (no places)\n", i);
        }
    }

    if (!strict) 
        err("Impossible to bring the system to diagonal dominance! No strict inequalities\n");

    apply_permutation(size, matrix, biases, indexes);
}

void solve_system(const uint8_t size, double** matrix, double* biases, double eps) {
    int16_t op_num = 0;
    double sigma = 0;

    double x[size];  // init x with ones
    for (uint8_t i = 0; i < size; i++) x[i] = 1.0;

    double error[size];
    double r[size];

    do {
        sigma = 0;

        for (uint8_t i = 0; i < size; i++) {
            double s = 0;

            for (uint8_t j = 0; j < size; j++) {
                if (i != j) s += matrix[i][j] * x[j];
            }

            r[i] = fabs(s + matrix[i][i] * x[i] - biases[i]);

            double new_x = (biases[i] - s) / matrix[i][i];

            error[i] = fabs(new_x - x[i]);

            if (r[i] > sigma) sigma = r[i];
            if (error[i] > sigma) sigma = error[i];

            x[i] = new_x;            
        }

        if (++op_num >= MAX_OPERATIONS_NUMBER) {
            printf("Max number of operations reached (%d)", MAX_OPERATIONS_NUMBER);
            break;
        }    
    } while (sigma > eps);

    printf("\nSolution found in %" PRId16 " iterations!\n", op_num);    
    printf("X: ");
    print_vector(size, x);
    printf("X abs. error: ");
    print_vector(size, error);
    printf("R: ");
    print_vector(size, r);
}