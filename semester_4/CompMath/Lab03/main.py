from input import *
from methods import *


def process_function():
    function_ind = choose_function()
    function = FUNCTIONS[function_ind]

    show_functions_graph({"f(x) = " + FUNCTIONS_TEXT[function_ind]: FUNCTIONS[function_ind]})

    method = FUNCTION_METHODS[choose_function_method()]

    bounds, precision = read_from_file()

    if not bounds or not precision:
        bounds = get_interval_bounds()
        precision = get_precision()

    res, partition = method(function, bounds, precision)

    if res is not None:
        print("\nSolved!")
        print("  Result =", round(res, 6))
        print("  Partition number:", partition, "\n")


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
    print(f'{"Hello!": ^50}')
    print(f'{"This is Matthew Ivanov Lab": ^50}')
    print(f'{"This app calculates the value of functions integrals": ^50}\n')

    start()
