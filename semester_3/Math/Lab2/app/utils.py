def print_star_line(message: str = "", message_len: int = 50):
    message = f" {message} " if message else message
    print(eval("f'%s'" % '{message:*^%s}' % message_len))


def print_empty_lines(num_lines):
    print("\n" * (num_lines - 1 if num_lines > 0 else 0))


def exit_app():
    print_empty_lines(1)
    print_star_line()
    print_star_line("Exit app")
    print_star_line()
    exit()


def get_input():
    while True:
        try:
            usr_inp = input("$ ").replace(" ", "")

            if not usr_inp:
                continue

            return usr_inp
        except EOFError:
            exit_app()
