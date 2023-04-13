from equations import *
from utils import check_keep_sign


def chord_method(function_ind, bounds, precision, *args, **kwargs):
    if check_keep_sign(EQUATIONS_SECOND_DERIVATIVES[function_ind], bounds):
        df_a = EQUATIONS_DERIVATIVES[function_ind](bounds[0])
        d2f_a = EQUATIONS_SECOND_DERIVATIVES[function_ind](bounds[0])

        if df_a * d2f_a < 0:
            return chord_method_with_fixed(EQUATIONS[function_ind], bounds[0], bounds[1], precision)
        else:
            return chord_method_with_fixed(EQUATIONS[function_ind], bounds[1], bounds[0], precision)

    return chord_method_without_fixed(EQUATIONS[function_ind], bounds[0], bounds[1], precision)


def chord_method_with_fixed(function, fixed_bound, x_approximation, precision):
    def formula():
        f_fix, f_x = function(fixed_bound), function(x_approximation)
        return (x_approximation * f_fix - fixed_bound * f_x) / (f_fix - f_x)

    iter_num = 0

    while abs(function(x_approximation)) > precision:
        x_approximation = formula()
        iter_num += 1

    return x_approximation, iter_num


def chord_method_without_fixed(function, a, b, precision):
    def formula():
        f_a, f_b = function(a), function(b)
        return (a * f_b - b * f_a) / (f_b - f_a)

    x = formula()
    iter_num = 0

    while abs(function(x)) > precision:
        if function(a) * function(x) < 0:
            b = x
        else:
            a = x

        x = formula()
        iter_num += 1

    return x, iter_num
