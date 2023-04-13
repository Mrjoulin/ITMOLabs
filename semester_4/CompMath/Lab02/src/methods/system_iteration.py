
def system_simple_iteration_method(suitable_systems, approx, precision, *args, **kwargs):
    for system in suitable_systems:
        x, y = approx
        diff, iter_num = None, 0
        exception = False

        while diff is None or diff > precision:
            try:
                new_x = system[0](x, y)
                new_y = system[1](x, y)
            except Exception:
                exception = True
                break

            diff = max(abs(new_x - x), abs(new_y - y))
            x, y = new_x, new_y
            iter_num += 1

        if not exception:
            return [x, y], iter_num, diff

    print("The sequence diverges!")

    return None, None, None
