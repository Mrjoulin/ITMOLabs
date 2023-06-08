from functions import *
from methods import FUNCTION_METHODS_DESCRIPTION
from .user_input import get_input


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
        print("Choose %s:" % option_name)

        try:
            option_ind = int(get_input())

            if option_ind <= 0 or option_ind > len(options):
                option_ind = None
        except ValueError:
            pass

    return option_ind - 1


def choose_continue_or_exit():
    return choose_option(["Continue", "Exit"], "option")


def choose_function():
    return choose_option(FUNCTIONS_TEXT, "function")


def choose_function_method():
    return choose_option(FUNCTION_METHODS_DESCRIPTION, "method")

