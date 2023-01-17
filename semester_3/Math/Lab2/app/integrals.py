import numpy as np

from .convex import cross_product


def get_box(dots):
    return [
        (
            min(dots, key=lambda x: x[0])[0],
            min(dots, key=lambda y: y[1])[1]
        ),
        (
            max(dots, key=lambda x: x[0])[0],
            max(dots, key=lambda y: y[1])[1]
        )
    ]


def inside_convex_polygon(points, point):
    previous_side = None

    for i in range(len(points)):
        a = [points[i], points[(i + 1) % len(points)], point]

        current_side = cross_product(a)

        if previous_side is None:
            previous_side = current_side
        elif previous_side * current_side < 0:
            return False
    return True


def find_area(points):
    t_points = np.array(points).T

    return np.abs(np.dot(t_points[0], np.roll(t_points[1], 1)) - np.dot(t_points[1], np.roll(t_points[0], 1))) / 2


def get_random_dots(points, num_dots):
    box = get_box(points)

    all_dots = np.array([])
    while len(all_dots) < num_dots:
        dots = np.random.uniform(box[0], box[1], (num_dots, 2))
        inside_polygon = [inside_convex_polygon(points, dot) for dot in dots]
        all_dots = np.concatenate((all_dots, dots[inside_polygon])) if all_dots.size else dots[inside_polygon]

    return all_dots[:num_dots]


def calculate_integral(func, points, num_dots):
    area = find_area(points)
    dots = get_random_dots(points, num_dots)

    try:
        func_res = np.apply_along_axis(lambda point: func(point[0], point[1]), 1, dots)
    except Exception:
        return None, None, None

    calc_dots = []
    results = []
    errors = []

    cur_num_dots = 1
    factor = 2

    while cur_num_dots < num_dots:
        if cur_num_dots * factor < num_dots:
            cur_num_dots *= factor
            cur_func_res = np.random.choice(func_res, cur_num_dots)
        else:
            cur_num_dots = num_dots
            cur_func_res = func_res

        func_mean = np.mean(cur_func_res)

        results.append(area * func_mean)

        error = area * np.sqrt(np.mean(np.square(cur_func_res - func_mean)) / (cur_num_dots - 1))
        errors.append(error)

        calc_dots.append(cur_num_dots)

    return calc_dots, results, errors
