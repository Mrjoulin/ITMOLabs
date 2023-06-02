from functions import FUNCTIONS_TEXT
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


def choose_function_or_input_manually():
    return choose_option(['Input dots manually', 'Choose function'], 'input type')


def choose_function():
    return choose_option(FUNCTIONS_TEXT, "function")

