from .parse_function import *
from .utils import *
from .convex import make_convex, validate_convex
from .integrals import calculate_integral
from .graph import show_graphs


def welcome_message():
    # Print welcome message
    print_empty_lines(2)
    print_star_line()
    print_star_line("Welcome to integral app")
    print_star_line("Created by Matthew Ivanov")
    print_star_line("Version 2")
    print_star_line()
    print_star_line("This is console app")
    print_star_line("To start integrate type \"start\"")
    print_star_line("To get more info type \"help\"")
    print_star_line()
    print_empty_lines(2)


def help_message():
    print_star_line()
    print_star_line("This app allows you to find integral")
    print_star_line("Of any function on any G - polygon")
    print_star_line("Monte-Carlo method")
    print_star_line()


def integral_process():
    print("Input function to integrate (type \"help\" to view operations):")
    func = None

    while func is None:
        text = get_input()

        if "help" in text:
            print(
                  "You can use:\n"
                  " - x, y as variables\n"
                  " - math operations +, -, *, /\n"
                  " - ^ for exponential (include 2^x, x^y and etc.)\n"
                  " - brackets\n"
                  " - trigonometry: sin, cos, tan, asin, acos, atan (argument in brackets!)\n\n"
                  "For example: x^2 + 10*x*y - 5*y^3"
            )
            continue

        func = parse_function(text)

        if func is None:
            print("Incorrect function! Type \"help\" to view operations")

    points = []

    print("Write G - polygon points. One point per line in format: x, y (ints or floats)")
    print("Or you can enter batch of points in format: x_1, y_1, x_2, y_2, ...")
    print("To finish typing, type \"next\", or \"help\" for examples")

    while True:
        points_text = get_input()

        if "help" in points_text:
            print("Example: 3.5, 4")
            print("Another example: 1, 2, 3.2, 2.5, -1, -2")
            continue
        elif "next" in points_text:
            if len(points) >= 3:
                break
            print("Enter at least 3 points to build a polygon")
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

        if len(points) >= 3:
            points = make_convex(points)
            removed = validate_convex(points)

            if removed:
                print("The figure is not convex! Removed:", removed)

        print("Done! Points:", points)

    num_dots = None

    print("Input number of dots in area (more is better accuracy):")
    while num_dots is None:
        try:
            num_dots = int(get_input())

            if num_dots <= 0:
                print("Number of dots should be positive!")
                num_dots = None
        except ValueError:
            print("Incorrect number! Separator should be integer (for example 10)")

    calc_dots, res, err = calculate_integral(func, points, num_dots)

    if res is not None and err is not None:
        print_star_line()
        print_star_line("Result for %d dots: %.4f" % (num_dots, res[-1]))
        print_star_line("Error for %d dots: %.4f" % (num_dots, err[-1]))
        print_star_line()
        print_empty_lines(1)

        show_graphs(calc_dots, res, err)
    else:
        print_star_line()
        print_star_line("Failed to calculate your integral :(")
        print_star_line()


def process_user_input():
    welcome_message()

    while True:
        user_input = get_input()

        if "help" in user_input:
            help_message()
        elif "start" in user_input or "run" in user_input:
            integral_process()
            print("Input \"start\" to calculate next integral")
        elif "exit" in user_input:
            exit_app()
        else:
            print("Command not found :(")
