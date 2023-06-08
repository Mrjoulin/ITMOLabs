from .Euler_improved import *
from .Runge_Kutta import *
from .Milna import *

FUNCTION_METHODS = [
    Euler_improved,
    Runge_Kutta,
    apply_Milna
]

FUNCTION_METHODS_DESCRIPTION = [
    "Euler improved",
    "Runge-Kutta 4th order",
    "Milna"
]
