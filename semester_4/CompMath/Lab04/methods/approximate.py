import numpy as np

from utils import *


def create_matrix(dots: list, num_coefficients: int, transform_x, transform_y):
    x = [0] * (2 * num_coefficients - 1)
    xy = [0] * num_coefficients

    for i in range(len(dots)):
        dot_x, dot_y = transform_x(dots[i][0]), transform_y(dots[i][1])

        cur_x = 1
        for j in range(2 * num_coefficients - 1):
            x[j] += cur_x

            if j < num_coefficients:
                xy[j] += cur_x * dot_y
            cur_x *= dot_x

    x_matrix = [x[i:i + num_coefficients] for i in range(num_coefficients)]

    return x_matrix, xy


def find_coefficients(dots: list, num_coefficients: int, transform_x=None, transform_y=None):
    linear = lambda x: x

    x_matrix, xy_bias = create_matrix(
        dots, num_coefficients,
        transform_x=transform_x or linear,
        transform_y=transform_y or linear
    )
    coefficients = np.linalg.solve(x_matrix, xy_bias)

    return np.around(coefficients, 3)


def linear_approximate(dots):
    func = np.poly1d(find_coefficients(dots, 2)[::-1])
    return str(func), func


def square_approximate(dots):
    func = np.poly1d(find_coefficients(dots, 3)[::-1])
    return str(func), func


def cube_approximate(dots):
    func = np.poly1d(find_coefficients(dots, 4)[::-1])
    return str(func), func


def exp_approximate(dots):
    a, b = find_coefficients(dots, 2, transform_y=np.log)
    func = lambda x: np.exp(a + b * x)
    return " " * (len(str(a)) + 2) + "%s x\n%s e" % (b, a), func


def log_approximate(dots):
    a, b = find_coefficients(dots, 2, transform_x=np.log)
    func = lambda x: a + b * np.log(x)
    return "%s ln(x) + %s" % (b, a), func


def power_approximate(dots):
    a, b = find_coefficients(dots, 2, transform_x=np.log, transform_y=np.log)
    func = lambda x: np.exp(a) * x ** b
    return " " * (len(str(a)) + 2) + "%s\n%s x" % (b, a), func


def get_std(dots, func):
    return np.sqrt(np.sum((func(dots[:, 0]) - dots[:, 1]) ** 2) / len(dots))


def get_correlation_coefficient(dots):
    x_diff = dots[:, 0] - np.mean(dots[:, 0])
    y_diff = dots[:, 1] - np.mean(dots[:, 1])

    return np.sum(x_diff * y_diff) / np.sqrt(np.sum(x_diff ** 2) * np.sum(y_diff ** 2))


def approximate(dots):
    dots = np.array(dots)

    approximate_func = [
        linear_approximate, square_approximate, cube_approximate,
        exp_approximate, log_approximate, power_approximate
    ]
    approximate_res = [func(dots) for func in approximate_func]
    stds = [get_std(dots, func) for _, func in approximate_res]

    best_approx = int(np.argmin(stds))

    cur_range = list(range(len(approximate_func)))
    cur_range.remove(best_approx)
    cur_range = [best_approx] + cur_range

    for i in cur_range:
        if i != best_approx and approximate_res[i][0] == approximate_res[best_approx][0]:
            continue

        print("%s point approximating function:" % ("Given" if i != cur_range[0] else "Best given"))
        print(approximate_res[i][0])
        print("Function std:", stds[i])

        if best_approx == 0:
            print("Pearson's correlation coefficient:", get_correlation_coefficient(dots))

        print_star_line("Table of results")
        print_table_of_results(dots, approximate_res[i][1])
        print("\n\n")

    show_functions_graph(dict(approximate_res), dots)
