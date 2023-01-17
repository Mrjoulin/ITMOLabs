import numpy as np


def cross_product(a):
    x1 = (a[1][0] - a[0][0])
    y1 = (a[1][1] - a[0][1])
    x2 = (a[2][0] - a[0][0])
    y2 = (a[2][1] - a[0][1])

    return x1 * y2 - y1 * x2


# Function to check if the polygon is convex polygon or not
def validate_convex(points):
    removed = []
    prev = 0
    i = 0

    while i < len(points):
        temp = [points[i], points[(i + 1) % len(points)], points[(i + 2) % len(points)]]
        curr = cross_product(temp)

        if curr == 0 or curr * prev < 0:
            removed.append(temp[1])
            points.remove(temp[1])
        else:
            prev = curr
            i += 1

    return removed


def cart2pol(point, center):
    rho = np.sqrt((point[0] - center[0])**2 + (point[1] - center[1])**2)
    phi = np.arctan2(point[1] - center[1], point[0] - center[0])
    return rho, phi


def pol2cart(point, center):
    x = point[0] * np.cos(point[1]) + center[0]
    y = point[0] * np.sin(point[1]) + center[1]
    return round(x, 3), round(y, 3)


def make_convex(points: list):
    center = np.mean(points, axis=0)

    convex_pol = sorted(
        map(lambda point: cart2pol(point, center), points),
        key=lambda p: p[1],
        reverse=True
    )

    return list(map(lambda point: pol2cart(point, center), convex_pol))

"""
def get_quantile_nearest_point(points, last, quantile):
    if quantile == 1:
        cond = lambda cur: cur[0] >= last[0] and cur[1] >= last[1]
    elif quantile == 2:
        cond = lambda cur: cur[0] >= last[0] and cur[1] <= last[1]
    elif quantile == 3:
        cond = lambda cur: cur[0] <= last[0] and cur[1] <= last[1]
    elif quantile == 4:
        cond = lambda cur: cur[0] <= last[0] and cur[1] >= last[1]
    else:
        return []

    quantile_points = list(filter(cond, points))

    if not quantile_points:
        return None

    return min(quantile_points, key=lambda point: (point[0] - last[0]) ** 2 + (point[1] - last[1]) ** 2)

def make_convex(points: list):
    convex = [points.pop(0)]

    while points:
        for quantile in range(1, 5):
            nearest_point = get_quantile_nearest_point(points, convex[-1], quantile)

            if nearest_point:
                convex.append(nearest_point)
                points.remove(nearest_point)
                break

    return convex
"""

