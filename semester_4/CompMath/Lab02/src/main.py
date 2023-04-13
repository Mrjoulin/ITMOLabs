from input import *
from utils import *


def process_equation():
    function_ind = choose_equation()
    method = EQUATION_METHODS[choose_equation_method()]

    show_equation_graphs(function_ind)

    bounds, precision = read_from_file(is_system=False)

    while bounds is None:
        bounds = get_interval_bounds()

        if not check_conditions(EQUATIONS[function_ind], EQUATIONS_DERIVATIVES[function_ind], bounds):
            bounds = None

    if precision is None:
        precision = get_precision()

    x, iter_num = method(function_ind, bounds, precision)

    if x is not None:
        print("\nFind solution!")
        print("  x =", round(x, 6))
        print("  f(x) =", round(EQUATIONS[function_ind](x), 6))
        print("  Iterations:", iter_num, "\n")


def process_system():
    system_ind = choose_system()

    show_functions_graph(SYSTEMS_GRAPHS[system_ind].copy())

    approx, precision = read_from_file(is_system=True)

    while approx is None:
        approx = get_approximations()
        suitable_systems = check_system_convergence(system_ind, approx)

        if not suitable_systems:
            print("Sufficient condition isn't satisfied in vicinity of the point!")
            approx = None

    if precision is None:
        precision = get_precision()

    solution, iter_num, diff = system_simple_iteration_method(suitable_systems, approx, precision)

    if solution is not None:
        print("\nFind solution!")
        print("  x = %.6f, y = %.6f" % (solution[0], solution[1]))
        print("  max |x_i - x_(i-1)| =", diff)
        print("  Iterations:", iter_num, "\n")


def start():
    while True:
        try:
            eq_or_system = choose_equation_or_system()

            if eq_or_system == 0:
                process_equation()
            else:
                process_system()

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
    print(f'{"This app solve non-linear equations and systems of equations": ^50}')

    start()
