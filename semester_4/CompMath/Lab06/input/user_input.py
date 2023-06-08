from utils import *


def get_input(in_line=False):
    while True:
        try:
            usr_inp = input("$ " if not in_line else "").replace(" ", "")

            if not usr_inp and not in_line:
                continue

            return usr_inp
        except EOFError:
            print_empty_lines(2)
            print_star_line()
            print_star_line("Goodbye! Have a nice day :)")
            print_star_line()
            exit()


def read_from_file():
    filename = None

    while filename is None:
        print("Enter filename to read input data (keep it empty to enter manually):", end=" ")

        filename = get_input(in_line=True)

        if not filename:
            break

        try:
            with open(filename, "r") as f:
                data = f.read().split("\n")

            if len(data) < 3:
                raise Exception("Not enough lines in file!")

            bounds = sorted(map(float, data[0].split()))
            if len(bounds) != 2 or bounds[0] == bounds[1]:
                raise Exception("Incorrect first line!")

            y0 = float(data[1])

            precision = float(data[2])
            if precision < 1e-6:
                raise Exception("Incorrect second line!")

            step = float(data[3])
            if step < 1e-3 or step > (bounds[1] - bounds[0]) / 2:
                raise Exception("Incorrect third line!")

            return bounds, y0, precision, step
        except Exception as e:
            print("Incorrect file!", e)
            filename = None

    return None


def get_interval_bounds():
    bounds = None

    while bounds is None:
        print("Input interval bounds separated by comma:", end=" ")

        try:
            bounds = list(map(float, get_input(in_line=True).split(',')))

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
            precision = float(get_input(in_line=True))

            if precision < 1e-6:
                precision = None
        except ValueError:
            pass

    return precision


def get_step(bounds):
    step = None

    while step is None:
        print("Input step (h):", end=" ")

        try:
            step = float(get_input(in_line=True))

            if step < 1e-3 or step > (bounds[1] - bounds[0]) / 2:
                print("Incorrect step size!")
                step = None
        except ValueError:
            pass

    return step


def get_y0(x0):
    y0 = None

    while y0 is None:
        print(f"Input y0 = y({x0}):", end=" ")

        try:
            y0 = float(get_input(in_line=True))
        except ValueError:
            pass

    return y0
