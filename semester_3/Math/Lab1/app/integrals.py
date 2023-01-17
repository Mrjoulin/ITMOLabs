from tensorflow import GradientTape, Variable

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


def rectangle_in_area(points, left_x, bottom_y, x_step, y_step):
    flag = inside_convex_polygon(points, [left_x, bottom_y])
    flag = flag and inside_convex_polygon(points, [left_x + x_step, bottom_y])
    flag = flag and inside_convex_polygon(points, [left_x + x_step, bottom_y + y_step])
    flag = flag and inside_convex_polygon(points, [left_x, bottom_y + y_step])

    return flag


def calculate_derivatives(func, x, y):
    x_v = Variable(x, dtype='float64')
    y_v = Variable(y, dtype='float64')

    with GradientTape(persistent=True) as second_grad:
        with GradientTape(persistent=True) as first_grad:
            res = func(x_v, y_v)
        dx = first_grad.gradient(res, x_v) or Variable(0.0, dtype='float64')
        dy = first_grad.gradient(res, y_v) or Variable(0.0, dtype='float64')

    d2x = second_grad.gradient(dx, x_v) or Variable(0.0, dtype='float64')
    d2y = second_grad.gradient(dy, y_v) or Variable(0.0, dtype='float64')
    dxy = second_grad.gradient(dx, y_v) or Variable(0.0, dtype='float64')
    dyx = second_grad.gradient(dy, x_v) or Variable(0.0, dtype='float64')

    return [dx, dy], [d2x, dxy, dyx, d2y]


def calculate_integral(func, points, separator):
    box = get_box(points)

    result = 0
    error = 0
    used_blocks = [[False] * separator for _ in range(separator)]

    x_step = (box[1][0] - box[0][0]) / separator
    y_step = (box[1][1] - box[0][1]) / separator

    for i in range(separator):
        for j in range(separator):
            x_left = box[0][0] + i * x_step
            y_bottom = box[0][1] + j * y_step

            x_middle = x_left + x_step / 2
            y_middle = y_bottom + y_step / 2

            try:
                if rectangle_in_area(points, x_left, y_bottom, x_step, y_step):
                    result += func(x_middle, y_middle) * x_step * y_step

                    first_derivatives, second_derivatives = calculate_derivatives(func, x_middle, y_middle)

                    error += x_step * first_derivatives[0] + y_step * first_derivatives[1]
                    error += (x_step**2 * second_derivatives[0] +
                              x_step * y_step * (second_derivatives[1] + second_derivatives[2])
                              + y_step**2 * second_derivatives[3]) / 2

                    used_blocks[i][j] = True
                    # Error for corner blocks (left and bottom)
                    if i > 0 and not used_blocks[i - 1][j]:
                        error += abs(func(x_middle - x_step, y_middle))
                        used_blocks[i - 1][j] = True
                    if j > 0 and not used_blocks[i][j - 1]:
                        error += abs(func(x_middle, y_middle - y_step))
                        used_blocks[i][j - 1] = True
                # if last column or top row
                elif i == separator - 1 and used_blocks[i - 1][j] or j == separator - 1 and used_blocks[i][j - 1]:
                    error += abs(func(x_middle, y_middle))
                    used_blocks[i][j] = True
            except Exception as e:
                return None, None

    error *= x_step * y_step

    return result, error
