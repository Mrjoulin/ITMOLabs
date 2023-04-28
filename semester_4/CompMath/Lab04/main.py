from input import *
from methods import *


def start():
    while True:
        try:
            dots = get_dots()
            approximate(dots)

            if choose_continue_or_exit() == 1:
                print("See you soon!")
                return
        except Exception as e:
            print("Some error happened: %s" % str(e))
            print("Start again")


if __name__ == '__main__':
    print_star_line('Hello!')
    print_star_line("This is Matthew Ivanov Lab")
    print_star_line("This app approximates  function given by the points")

    start()
