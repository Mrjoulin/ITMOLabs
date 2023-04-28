def print_star_line(message: str = "", message_len: int = 50):
    message = f" {message} " if message else message
    print(eval("f'%s'" % '{message:*^%s}' % message_len))


def print_empty_lines(num_lines):
    print("\n" * (num_lines - 1 if num_lines > 0 else 0))


def print_table_of_results(dots, func):
    cell_len = 15
    num_columns = 4

    def print_line():
        print(*['-' * cell_len for _ in range(num_columns)], sep='+')

    def print_values(values):
        for i, val in enumerate(values):
            print(eval("f'%s'" % '{val: ^%s}' % cell_len), end='|' if i < len(values) - 1 else '\n')

    title = ['x_i', 'y_i', 'φ(x_i)', 'ε_i']
    print_values(title)

    for dot in dots:
        print_line()
        f_x = round(func(dot[0]))
        print_values([dot[0], dot[1], f_x, f_x - dot[1]])
