START_PARTITION_NUMBER = 4
MAX_PARTITION_NUMBER = 1e9


def check_runge_rule(res_array, precision, k):
    if len(res_array) < 2:
        return True

    return abs((res_array[-1] - res_array[-2]) / (2**k - 1)) > precision


def get_step(bounds, partition_number):
    return (bounds[1] - bounds[0]) / partition_number
