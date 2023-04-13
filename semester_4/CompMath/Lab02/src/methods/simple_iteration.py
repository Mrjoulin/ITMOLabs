from .chord import chord_method_without_fixed
from equations import *
from utils import check_keep_sign, x_in_bounds, show_functions_graph


def find_derivative_max(function_ind, bounds):
    df = EQUATIONS_DERIVATIVES[function_ind]
    d2f = EQUATIONS_SECOND_DERIVATIVES[function_ind]

    if check_keep_sign(d2f, bounds):
        return max(abs(df(bounds[0])), abs(df(bounds[1])))

    x, _ = chord_method_without_fixed(d2f, bounds[0], bounds[1], 1e-3)

    return max(abs(df(x)), abs(df(bounds[0])), abs(df(bounds[1])))


def simple_iteration_method(function_ind, bounds, precision, *args, **kwargs):
    func = EQUATIONS[function_ind]
    sign = int(func(bounds[0]) > func(bounds[1])) * 2 - 1
    _lambda = sign / find_derivative_max(function_ind, bounds)

    phi = lambda _x: _x + _lambda * func(_x)

    show_functions_graph({"f(x)": func, "phi(x)": phi, "y = x": lambda _x: _x})

    x_prev, x = bounds[0], phi(bounds[0])
    iter_num = 0

    while abs(x - x_prev) > precision or abs(func(x)) > precision:
        x_prev, x = x, phi(x)
        iter_num += 1

        if not x_in_bounds(x, bounds):
            print("The sequence diverges!")
            return None, None

    return x, iter_num
