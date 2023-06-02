def print_star_line(message: str = "", message_len: int = 50):
    message = f" {message} " if message else message
    print(eval("f'%s'" % '{message:*^%s}' % message_len))


def print_empty_lines(num_lines):
    print("\n" * (num_lines - 1 if num_lines > 0 else 0))


def print_table(table, start_number=0):
    cell_len = 5
    num_columns = table.shape[1] + 1

    def print_line():
        print(*['-' * cell_len for _ in range(num_columns)], sep='+')

    def print_values(values):
        for i, val in enumerate(values):
            print(eval("f'%s'" % '{val: ^%s}' % cell_len), end='|' if i < len(values) - 1 else '\n')

    title = ['i', 'x_i', 'y_i'] + [f'Δ^{i}' for i in range(1, table.shape[1] - 1)]
    print_values(title)

    cur = start_number

    for row in table:
        print_line()
        print_values([cur] + list(map(lambda x: round(x, 2), row)))
        cur += 1

    print_empty_lines(2)
