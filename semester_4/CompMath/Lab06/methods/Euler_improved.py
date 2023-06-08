from functions import *
from utils import check_runge_rule
import numpy as np

EULER_IMPROVED_P = 2


def Euler_improved(function_ind, bounds, y0, precision, step):
    prev_res_x, prev_res_y = apply_Euler_improved(function_ind, bounds, y0, step)
    res_x, res_y = prev_res_x, prev_res_y

    while step > 1e-3:
        step /= 2
        res_x, res_y = apply_Euler_improved(function_ind, bounds, y0, step)

        if check_runge_rule(prev_res_y[-1], res_y[-1], precision, p=EULER_IMPROVED_P):
            return res_x, res_y

        prev_res_x, prev_res_y = res_x, res_y

    return res_x, res_y


def apply_Euler_improved(function_ind, bounds, y0, step):
    f = FUNCTIONS[function_ind]
    cur = bounds[0]
    res_x, res_y = [cur], [y0]

    while cur + step <= bounds[1]:
        y = res_y[-1]
        y = y + step / 2 * (f(cur, y) + f(cur, y + step * f(cur, y)))
        res_y.append(y)

        cur += step
        res_x.append(cur)

    return np.array(res_x), np.array(res_y)
