def check_conditions(function, derivative, bounds):
    f_a, f_b = function(bounds[0]), function(bounds[1])

    if f_a * f_b > 0:
        print("Equation has 0 or more solutions on the interval!")
        return False

    if f_a == 0 or f_b == 0:
        print("Solution is already on bounds of interval!")
        print("f(%s) = 0" % ('a' if f_a == 0 else 'b'))
        return False

    if not check_keep_sign(derivative, bounds):
        print("Derivative does not keep sign on the interval!")
        return False

    return True


def check_keep_sign(function, bounds):
    cur, step = bounds[0], 0.1
    f_a = function(cur)

    while cur < bounds[1]:
        cur = min(cur + step, bounds[1])

        if function(cur) * f_a < 0:
            return False

    return True


def x_in_bounds(x, bounds):
    return bounds[0] - 2 <= x <= bounds[1] + 2
