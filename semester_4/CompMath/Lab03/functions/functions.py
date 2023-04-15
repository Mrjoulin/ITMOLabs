from numpy import arctan, log


FUNCTIONS = [
    lambda _x: 5 * _x**3 - 19 * _x**2 + 4 * _x + 15,
    lambda _x: 2 * _x**2 - 10 * _x - 3,
    lambda _x: 3 * arctan(5 * _x) + 1,
    lambda _x: 2 * 3 ** _x - 4,
    lambda _x: 3 * _x**5 + _x**4 - 0.4 * _x**3 + 2.8 * _x**2 + 10.4 * _x - 8
]

FUNCTIONS_TEXT = [
    "5x^3 - 19x^2 + 4x + 10",
    "2x^2 - 10x - 3",
    "3 * arctan(5x) + 1",
    "2 * 3^x - 4",
    "3x^5 + x^4 - 0.4x^3 + 2.8x^2 + 10.4x - 8"
]
