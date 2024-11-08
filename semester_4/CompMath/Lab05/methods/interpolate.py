from utils import *
from functions import *


def lagrange_interpolate(dots):
    def func(x, verbose=0):
        res = 0
        for i in range(len(dots)):
            cur = dots[i][1]
            for j in range(len(dots)):
                cur *= (x - dots[j][0]) / (dots[i][0] - dots[j][0]) if i != j else 1
            res += cur
        return res

    return func


def get_end_differences(dots):
    table = np.zeros((len(dots), len(dots) + 1))
    table[:, :2] = dots

    for i in range(1, len(dots)):
        table[:-i, i + 1] = np.diff(dots[:, 1], n=i)

    return table


def gauss_interpolate(dots, target):
    center = len(dots) // 2
    x0 = dots[center][0]

    diff = get_end_differences(dots)
    print_table(diff, -center)

    def func(x, verbose=0):
        # use first and second gauss formula
        t = (x - x0) / abs(x0 - dots[center + 2 * (x > x0) - 1][0])
        res = dots[center][1]
        cur_coef, cur_diff = 1, 0

        used = [res]

        for i in range(min(center, len(dots) // 2)):
            # odd step
            cur_coef *= (t + cur_diff) / (2 * i + 1)
            res += cur_coef * diff[center - i + ((x > x0) - 1)][2 * (i + 1)]

            used.append(round(diff[center - i + ((x > x0) - 1)][2 * (i + 1)], 2))

            # even step
            if 2 * (i + 1) < len(dots):
                cur_coef *= (t - (cur_diff + 2 * (x > x0) - 1)) / (2 * i + 2)
                res += cur_coef * diff[center - i - 1][2 * (i + 1) + 1]
                used.append(round(diff[center - i - 1][2 * (i + 1) + 1], 2))

            cur_diff += 2 * (x > x0) - 1

        if verbose > 0:
            print('Used differences:', ', '.join(map(str, used)))

        return res

    return func


def interpolate(dots, target, func_ind=None):
    lagrange = lagrange_interpolate(dots)
    gauss = gauss_interpolate(dots, target)

    result_functions = {
        "Lagrange": lagrange,
        "Gauss": gauss
    }

    print("Get interpolation results!\n")
    for i, (func_name, func) in enumerate(result_functions.items()):
        print(f"{i + 1}. {func_name} interpolation:")
        print(f"P({target}) = {func(target, verbose=1)}\n")

    if func_ind is not None:
        print("Exact function value:", FUNCTIONS[func_ind](target))
        result_functions[FUNCTIONS_TEXT[func_ind]] = FUNCTIONS[func_ind]

    show_functions_graph_by_dots(result_functions, dots=dots)
    print_empty_lines(1)
