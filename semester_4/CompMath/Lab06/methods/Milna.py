from .Runge_Kutta import *
import numpy as np


def Milna(function_ind, bounds, y0, precision, step):
    function_c = C_FUNCTIONS[function_ind](bounds[0], y0)
    correct_func = CORRECT_FUNCTIONS[function_ind](function_c)

    res_x, res_y = apply_Milna(function_ind, bounds, y0, precision, step)

    while np.max(np.abs(res_y - correct_func(res_x))) > precision and step > 1e-3:
        step /= 2
        res_x, res_y = apply_Milna(function_ind, bounds, y0, precision, step)

    return res_x, res_y


def apply_Milna(function_ind, bounds, y0, precision, step):
    if step > (bounds[1] - bounds[0]) / 4:
        print("Step is too big for this method! Just using Runge-Kutta method...")
        return apply_Runge_Kutta(function_ind, bounds, y0, step)

    res_x, res_y = Runge_Kutta(function_ind, [bounds[0], bounds[0] + 3 * step], y0, precision, step)
    res_x, res_y = list(res_x)[:4], list(res_y)[:4]

    step = res_x[1] - res_x[0]

    f = FUNCTIONS[function_ind]
    cur = res_x[-1] + step

    while cur <= bounds[1]:
        prev_y = res_y[-4] + 4/3 * step * (
                2 * f(res_x[-3], res_y[-3]) - f(res_x[-2], res_y[-2]) + 2 * f(res_x[-1], res_y[-1])
        )
        y = res_y[-2] + step/3 * (f(res_x[-2], res_y[-2]) + 4 * f(res_x[-1], res_y[-1]) + f(cur, prev_y))

        num_it = 0
        while abs(y - prev_y) > precision and num_it < 100 and not np.isnan(y) and not np.isinf(y):
            prev_y = y
            y = res_y[-2] + step / 3 * (f(res_x[-2], res_y[-2]) + 4 * f(res_x[-1], res_y[-1]) + f(cur, prev_y))
            num_it += 1

        if np.isnan(y) or np.isinf(y):
            print("Method is not applicable to this interval!")
            return None, None

        res_x.append(cur)
        res_y.append(y)
        cur += step

    return np.array(res_x), np.array(res_y)
