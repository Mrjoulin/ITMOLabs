from .rectangle import *
from .trapezoid import *
from .simpson import *

FUNCTION_METHODS = [
    left_rectangle_method,
    middle_rectangle_method,
    right_rectangle_method,
    trapezoid_method,
    simpson_method
]

FUNCTION_METHODS_DESCRIPTION = [
    "Left rectangle",
    "Middle rectangle",
    "Right rectangle",
    "Trapezoid",
    "Simpson"
]
