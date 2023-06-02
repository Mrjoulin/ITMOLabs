from .validate_dots import *
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


def get_dots():
    return np.array(read_dots_from_file() or get_dots_manually())


def read_dots_from_file():
    filename = None

    while filename is None:
        print("Enter filename to read function points (keep it empty to enter manually):", end=" ")

        filename = get_input(in_line=True)
        if not filename:
            break

        try:
            with open(filename, "r") as f:
                data = f.read().split("\n")

            points = []
            for i, points_text in enumerate(data):
                if not points_text:
                    continue

                new_points = parse_points(points_text)

                if new_points is None:
                    print("Incorrect points format in line %s!" % (i + 1))
                    return None

                points += new_points

            removed = validate_repeating_dots(points)

            if removed:
                print("Repeating dots not allowed! Removed:", removed)

            print("Done! Points:", points)

            return points
        except Exception:
            print("Incorrect filename or file isn't readable!")
            filename = None
    return None


def get_dots_manually():
    points = []

    print("Input function points. One point per line in format: x, y (ints or floats)")
    print("Or you can enter batch of points in format: x_1, y_1, x_2, y_2, ...")
    print("To finish typing, type \"done\", or \"help\" for examples")

    while True:
        points_text = get_input()

        if "help" in points_text:
            print("Example: 3.5, 4")
            print("Another example: 1, 2, 3.2, 2.5, -1, -2")
            print("If you write wrong point, use \"clear\" command to remove points")
            continue
        elif "done" in points_text:
            if len(points) >= 4:
                break
            print("Enter at least 4 points")
            continue
        elif "clear" in points_text:
            print("Points cleared!")
            points = []
            continue

        new_points = parse_points(points_text)

        if new_points is None:
            print("Incorrect points input! Type \"help\" to view examples")
            continue

        points += new_points

        removed = validate_repeating_dots(points)
        if removed:
            print("Repeating dots not allowed! Removed:", removed)

        print("Done! Points:", points)

    return points


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


def get_number_of_points():
    num_dots = None

    while num_dots is None:
        print("Input number of points:", end=" ")

        try:
            num_dots = int(get_input(in_line=True))

            if num_dots < 4 or num_dots > 1000:
                print("Number of points should be in range 4..1000")
                num_dots = None
        except ValueError:
            pass

    return num_dots


def get_target_point(bounds):
    target = None

    while target is None:
        print("Input target point:", end=" ")

        try:
            target = float(get_input(in_line=True))

            if target < bounds[0] or bounds[1] < target:
                print("Target point is out of bounds!")
                target = None

        except ValueError:
            pass

    return target

