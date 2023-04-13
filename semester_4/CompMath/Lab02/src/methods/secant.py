from equations import *
from utils import check_keep_sign


def secant_method(function_ind, bounds, precision, *args, **kwargs):
    d2f = EQUATIONS_SECOND_DERIVATIVES[function_ind]

    if not check_keep_sign(d2f, bounds):
        print("The sufficient condition for convergence of the method is not fulfilled!")
        return None, None

    df = EQUATIONS_DERIVATIVES[function_ind]

    if df(bounds[0]) == 0 or df(bounds[1]) == 0:
        print("Derivative must not be equal to 0!")
        return None, None

    func = EQUATIONS[function_ind]

    if func(bounds[0]) * d2f(bounds[0]) > 0:
        x_prev = bounds[0]
        x = min(x_prev + 0.1, bounds[1])
    else:
        x_prev = bounds[1]
        x = max(x_prev - 0.1, bounds[0])

    iter_num = 0

    while abs(func(x)) > precision:
        f_x, f_x_prev = func(x), func(x_prev)
        x_prev, x = x, (x_prev * f_x - x * f_x_prev) / (f_x - f_x_prev)
        iter_num += 1

    return x, iter_num
