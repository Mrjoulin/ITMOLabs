import numpy as np

from utils import *


def get_partition(bounds, partition_number, partition_type):
    step = get_step(bounds, partition_number)

    if partition_type == 'left':
        return np.linspace(bounds[0], bounds[1] - step, partition_number)
    elif partition_type == 'right':
        return np.linspace(bounds[0] + step, bounds[1], partition_number)
    else:
        return np.linspace(bounds[0] + step / 2, bounds[1] - step / 2, partition_number)


def left_rectangle_method(function, bounds, precision, *args, **kwargs):
    return rectangle_method(function, bounds, precision, partition_type='left')


def right_rectangle_method(function, bounds, precision, *args, **kwargs):
    return rectangle_method(function, bounds, precision, partition_type='right')


def middle_rectangle_method(function, bounds, precision, *args, **kwargs):
    return rectangle_method(function, bounds, precision, partition_type='middle')


def rectangle_method(function, bounds, precision, partition_type):
    partition_number = START_PARTITION_NUMBER
    runge_rule_k = 1 if partition_type in ['left', 'right'] else 2
    res = []

    while check_runge_rule(res, precision, k=runge_rule_k) or partition_number > MAX_PARTITION_NUMBER:
        partition_number *= 2

        partition = get_partition(bounds, partition_number, partition_type)
        res.append(sum(function(partition)) * get_step(bounds, partition_number))

        if len(res) > 2:
            res.pop(0)

    return res[1], partition_number
