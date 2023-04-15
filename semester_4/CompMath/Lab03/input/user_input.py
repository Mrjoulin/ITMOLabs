def read_from_file():
    filename = None

    while filename is None:
        print("Enter filename to read input data (keep it empty to enter manually):", end=" ")

        filename = input()

        if not filename:
            break

        try:
            with open(filename, "r") as f:
                data = f.read().split("\n")

            if len(data) < 2:
                raise Exception("Not enough lines in file!")

            bounds = list(map(float, data[0].split()))

            if len(bounds) != 2 or bounds[0] == bounds[1]:
                raise Exception("Incorrect first line!")

            precision = float(data[1])

            if precision < 1e-6:
                raise Exception("Incorrect second line!")

            return bounds, precision
        except Exception as e:
            print("Incorrect file!", e)
            filename = None

    return None, None


def get_interval_bounds():
    bounds = None

    while bounds is None:
        print("Input interval bounds separated by a space:", end=" ")

        try:
            bounds = list(map(float, input().split()))

            if len(bounds) != 2 or bounds[0] == bounds[1]:
                bounds = None
        except ValueError:
            pass

    if bounds[0] > bounds[1]:
        print("Interval bounds swapped")
        bounds = bounds[::-1]

    return bounds


def get_precision():
    precision = None

    while precision is None:
        print("Input precision (eps):", end=" ")

        try:
            precision = float(input())

            if precision < 1e-6:
                precision = None
        except ValueError:
            pass

    return precision


