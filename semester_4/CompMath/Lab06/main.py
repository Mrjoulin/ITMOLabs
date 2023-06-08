from input import *
from methods import *
from utils import *


def process_function():
    function_ind = choose_function()
    method = FUNCTION_METHODS[choose_function_method()]

    data_from_file = read_from_file()

    if data_from_file is None:
        bounds = get_interval_bounds()
        y0 = get_y0(bounds[0])
        precision = get_precision()
        step = get_step(bounds)
    else:
        bounds, y0, precision, step = data_from_file

    res_x, res_y = method(function_ind, bounds, y0, precision, step)

    if res_x is not None and res_y is not None:
        function_c = C_FUNCTIONS[function_ind](bounds[0], y0)
        correct_func = CORRECT_FUNCTIONS[function_ind](function_c)

        print_empty_lines(2)
        print_star_line("Table of results")
        print_table(res_x=res_x, res_y=res_y, corr_y=correct_func(res_x))
        print("Final step:", res_x[1] - res_x[0])

        # Show graph
        dots = np.concatenate((np.expand_dims(res_x, axis=1), np.expand_dims(res_y, axis=1)), axis=1)
        predicted_func = gauss_interpolate(dots)
        functions_info = {
            "Correct": correct_func,
            "Predicted": predicted_func
        }
        show_functions_graph_by_dots(functions_info, dots, step)


def start():
    while True:
        try:
            process_function()

            if choose_continue_or_exit() == 1:
                print("See you soon!")
                return
        except EOFError:
            print("Goodbye! Have a nice day :)")
            return
        except Exception as e:
            print("Some error happened: %s" % str(e))
            print("Start again")


if __name__ == '__main__':
    print_star_line("Hello!")
    print_star_line("This is Matthew Ivanov Lab")
    print_star_line("This app solves the Cauchy problem")
    print_star_line("for ordinary differential equations")
    print_empty_lines(2)

    start()
