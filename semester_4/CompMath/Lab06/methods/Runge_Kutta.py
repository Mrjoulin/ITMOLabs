from functions import *
from utils import check_runge_rule
import numpy as np

RUNGE_KUTTA_P = 4


def Runge_Kutta(function_ind, bounds, y0, precision, step):
    prev_res_x, prev_res_y = apply_Runge_Kutta(function_ind, bounds, y0, step)
    res_x, res_y = prev_res_x, prev_res_y

    while step > 1e-3:
        step /= 2
        res_x, res_y = apply_Runge_Kutta(function_ind, bounds, y0, step)

        if check_runge_rule(prev_res_y[-1], res_y[-1], precision, p=RUNGE_KUTTA_P):
            return res_x, res_y

        prev_res_x, prev_res_y = res_x, res_y

    return res_x, res_y


def apply_Runge_Kutta(function_ind, bounds, y0, step):
    f = FUNCTIONS[function_ind]
    cur = bounds[0]
    res_x, res_y = [cur], [y0]

    while cur + step <= bounds[1]:
        y = res_y[-1]
        k1 = step * f(cur, y)
        k2 = step * f(cur + step / 2, y + k1 / 2)
        k3 = step * f(cur + step / 2, y + k2 / 2)
        k4 = step * f(cur + step, y + k3)

        y = y + 1 / 6 * (k1 + 2 * k2 + 2 * k3 + k4)
        res_y.append(y)

        cur += step
        res_x.append(cur)

    return np.array(res_x), np.array(res_y)
