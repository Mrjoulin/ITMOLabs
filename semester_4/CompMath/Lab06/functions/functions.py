from numpy import arctan, log, exp


FUNCTIONS = [
    lambda _x, _y: _y + _x,
    lambda _x, _y: _x**2 - 2 * _y,
    lambda _x, _y: 3 * _x + 2 * _x * _y,
    # lambda _x, _y: 2 * 3 ** _x - 4,
]

CORRECT_FUNCTIONS = [
    lambda _c: lambda _x: _c * exp(_x) - _x - 1,
    lambda _c: lambda _x: _c * exp(-2 * _x) + (_x ** 2) / 2 - _x / 2 + 1 / 4,
    lambda _c: lambda _x: _c * exp(_x ** 2) - 3/2
]

C_FUNCTIONS = [  # input x_0, y_0
    lambda _x, _y: (_y + _x + 1) / exp(_x),
    lambda _x, _y: (_y - (_x ** 2) / 2 + _x / 2 - 1 / 4) / exp(-2 * _x),
    lambda _x, _y: (_y + 3/2) / exp(_x ** 2)
]

FUNCTIONS_TEXT = [
    "x + y",
    "x^2 - 2y",
    "3x + 2xy"
]
