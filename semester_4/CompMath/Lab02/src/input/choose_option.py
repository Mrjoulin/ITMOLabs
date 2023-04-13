from equations import *
from methods import *


def choose_option(options, option_name):
    for i, option in enumerate(options):
        if not isinstance(option, list):
            print("(%d) %s" % (i + 1, option))
            continue

        print("(%d)" % (i + 1))
        for sub_option in option:
            print("  |", sub_option)
        print()

    option_ind = None

    while option_ind is None:
        print("Choose %s:" % option_name, end=" ")

        try:
            option_ind = int(input())

            if option_ind <= 0 or option_ind > len(options):
                option_ind = None
        except ValueError:
            pass

    return option_ind - 1


def choose_continue_or_exit():
    return choose_option(["Continue", "Exit"], "option")


def choose_equation_or_system():
    return choose_option(["Equation", "System of equations"], "option")


def choose_equation():
    return choose_option(EQUATIONS_TEXT, "equation")


def choose_system():
    return choose_option(SYSTEMS_TEXT, "system")


def choose_equation_method():
    return choose_option(EQUATION_METHODS_DESCRIPTION, "method")
