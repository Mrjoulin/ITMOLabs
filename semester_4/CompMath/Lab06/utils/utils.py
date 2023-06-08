
def check_runge_rule(y_h, y_h2, precision, p):
    return abs((y_h - y_h2) / (2**p - 1)) <= precision


def print_star_line(message: str = "", message_len: int = 50):
    message = f" {message} " if message else message
    print(eval("f'%s'" % '{message:*^%s}' % message_len))


def print_empty_lines(num_lines):
    print("\n" * (num_lines - 1 if num_lines > 0 else 0))


def print_table(res_x, res_y, corr_y):
    cell_len = 11
    num_columns = 4

    def print_line():
        print(*['-' * cell_len for _ in range(num_columns)], sep='+')

    def print_values(values):
        for i, val in enumerate(values):
            print(eval("f'%s'" % '{val: ^%s}' % cell_len), end='|' if i < len(values) - 1 else '\n')

    title = ['i', 'x_i', 'y_predict', 'y_correct']
    print_values(title)

    for i, row in enumerate(zip(res_x, res_y, corr_y)):
        print_line()
        print_values([i] + list(map(lambda x: round(x, 5), row)))

    print_empty_lines(2)
