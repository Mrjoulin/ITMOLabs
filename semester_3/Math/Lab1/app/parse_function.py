from tensorflow import sin, cos, tan, asin, acos, atan
from itertools import combinations
from collections import Counter


def is_function_text_correct(function_text: str):
    available = [
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
        "+", "-", "*", "/", "^", "(", ")", "x", "y", "sin", "cos", "tan", "asin", "acos", "atan"
    ]

    for symbol in available:
        function_text = function_text.replace(symbol, "")

    return not bool(function_text)


def parse_function(function_text: str):
    if not is_function_text_correct(function_text):
        return None

    try:
        function_text = "lambda x, y: " + function_text
        function_text = function_text.replace("^", "**")
        fun = eval(function_text)

        try:
            fun(0, 0)
        except SyntaxError:
            return None
        except NameError:
            return None
        except Exception:
            pass
        return fun
    except Exception as e:
        return None


def parse_points(points_text):
    try:
        points = eval(f"[{points_text}]")

        if len(points) % 2:
            raise Exception()

        points_pairs = [[]]

        for point in points:
            if isinstance(point, int) or isinstance(point, float):
                if len(points_pairs[-1]) < 2:
                    points_pairs[-1].append(point)
                else:
                    points_pairs.append([point])
            else:
                raise Exception()

        return list(map(lambda x: tuple(x), points_pairs))
    except Exception as e:
        return None


def validate_repeating_dots(points: list):
    repeat = list(filter(lambda item: item[1] > 1, Counter(points).items()))

    for repeat_point in repeat:
        for _ in range(repeat_point[1] - 1):
            points.remove(repeat_point[0])

    return list(map(lambda x: x[0], repeat))


def validate_points_in_line(points: list):
    def get_area(p1, p2, p3):
        # Ax * (By - Cy) + Bx * (Cy - Ay) + Cx * (Ay - By)
        return p1[0] * (p2[1] - p3[1]) + p2[0] * (p3[1] - p1[1]) + p3[0] * (p1[1] - p2[1])

    triplets = list(combinations(points, 3))
    removed = []

    for triplet in triplets:
        any_removed = any([dot in removed for dot in triplet])

        if not any_removed or not get_area(*triplet):
            if triplet[0][0] == triplet[1][0]:  # on same x vertical
                triplet = sorted(triplet, key=lambda x: x[1])
            else:
                triplet = sorted(triplet, key=lambda x: x[0])

            removed.append(triplet[1])
            points.remove(triplet[1])

    return removed


