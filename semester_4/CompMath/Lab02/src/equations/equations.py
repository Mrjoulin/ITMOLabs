from numpy import arctan, log


EQUATIONS = [
    lambda _x: 5 * _x**3 - 19 * _x**2 + 4 * _x + 15,
    lambda _x: 2 * _x**2 - 10 * _x - 3,
    lambda _x: 3 * arctan(5 * _x) + 1,
    lambda _x: 2 * 3 ** _x - 4,
    lambda _x: 3 * _x**5 + _x**4 - 0.4 * _x**3 + 2.8 * _x**2 + 10.4 * _x - 8
]

EQUATIONS_TEXT = [
    "5x^3 - 19x^2 + 4x + 10 = 0",
    "2x^2 - 10x - 3 = 0",
    "3 * arctan(5x) + 1 = 0",
    "2 * 3^x - 4 = 0",
    "3x^5+x^4-0.4x^3+2.8x^2+10.4x-8"
]

EQUATIONS_DERIVATIVES = [
    lambda _x: 15 * _x**2 - 38 * _x + 4,
    lambda _x: 4 * _x - 10,
    lambda _x: 15 / (25 * _x ** 2 + 1),
    lambda _x: 2 * 3 ** _x * log(3),
    lambda _x: 15 * _x**4 + 4 * _x**3 - 1.2 * _x**2 + 5.6 * _x + 10.4
]

EQUATIONS_SECOND_DERIVATIVES = [
    lambda _x: 30 * _x - 38,
    lambda _x: 0 * _x + 4,
    lambda _x: -750 * _x / (25 * _x ** 2 + 1) ** 2,
    lambda _x: 2 * 3 ** _x * log(3) ** 2,
    lambda _x: 60 * _x**3 + 12 * _x**2 - 2.4 * _x + 5.6
]
