import numpy as np

from utils import *


def simpson_method(function, bounds, precision, *args, **kwargs):
    partition_number = START_PARTITION_NUMBER
    res = []

    while check_runge_rule(res, precision, k=4) or partition_number > MAX_PARTITION_NUMBER:
        partition_number *= 2

        partition = np.linspace(bounds[0], bounds[1], partition_number + 1)
        func_res = function(partition)
        func_res[1:-1] *= 2
        func_res[1::2] *= 2

        res.append(sum(func_res) * get_step(bounds, partition_number) / 3)

        if len(res) > 2:
            res.pop(0)

    return res[1], partition_number
