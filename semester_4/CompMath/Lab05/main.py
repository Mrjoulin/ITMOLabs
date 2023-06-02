from input import *
from methods import *
from functions import *
from utils import *


def process_function():
    function_ind = choose_function()
    function = FUNCTIONS[function_ind]

    show_functions_graph({"f(x) = " + FUNCTIONS_TEXT[function_ind]: function})

    bounds = get_interval_bounds()
    num_dots = get_number_of_points()

    dots = np.expand_dims(np.linspace(*bounds, num=num_dots), axis=1)
    dots = np.concatenate((dots, function(dots)), axis=1)
    target = get_target_point(bounds)

    interpolate(dots, target, func_ind=function_ind)


def start():
    while True:
        #try:
        input_type = choose_function_or_input_manually()

        if not input_type:
            dots = get_dots()
            target = get_target_point([dots[0][0], dots[-1][0]])
            interpolate(dots, target)
        else:
            process_function()

        if choose_continue_or_exit() == 1:
            print("See you soon!")
            return
        #except Exception as e:
        #    print("Some error happened: %s" % str(e))
        #    print("Start again")


if __name__ == '__main__':
    print_star_line('Hello!')
    print_star_line("This is Matthew Ivanov Lab")
    print_star_line("This app interpolates function given by the points")

    start()
