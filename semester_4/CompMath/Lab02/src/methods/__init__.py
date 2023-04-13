from .chord import chord_method
from .secant import secant_method
from .simple_iteration import simple_iteration_method
from .system_iteration import system_simple_iteration_method

EQUATION_METHODS = [
    chord_method,
    secant_method,
    simple_iteration_method
]

EQUATION_METHODS_DESCRIPTION = [
    "Chord method",
    "Secant method",
    "Simple Iteration Method"
]
