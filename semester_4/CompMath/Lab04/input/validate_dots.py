from collections import Counter


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

    points.sort(key=lambda p: p[0])

    return list(map(lambda x: x[0], repeat))
