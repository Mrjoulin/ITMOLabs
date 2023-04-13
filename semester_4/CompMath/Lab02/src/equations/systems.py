from math import sqrt
import numpy as np

SYSTEMS = [
    [
        lambda _x, _y: 4 * _x ** 2 - 4 * _x + 3 * _y ** 2,
        lambda _x, _y: 16 * _x**3 + 5 * _y**2 - 2 * _y - 3
    ],
    [
        lambda _x, _y: 2 * _x + 4 * _y**2 - 10,
        lambda _x, _y: 5 * (_x + 1) ** 2 - 2 * _y - 1
    ]
]

SYSTEMS_TEXT = [
    [
        "4x^2 - 4x + 3y^2 = 0",
        "16x^3 + 5y^2 - 2y - 3 = 0"
    ],
    [
        "2x + 4y^2 - 10 = 0",
        "5(x + 1)^2 - 2y -1 = 0"
    ]
]

SYSTEMS_GRAPHS = [
    {
        "x = f_1(y) (1 half)": lambda _y: (np.sqrt(-3 * _y**2 + 1) + 1) / 2,
        "x = f_1(y) (2 half)": lambda _y: (-np.sqrt(-3 * _y**2 + 1) + 1) / 2,
        "x = f_2(y)": lambda _y: np.cbrt((-5 * _y**2 + 2 * _y + 3) / 16),
        "by_y": True
    },
    {
        "y = f_1(x) (1 half)": lambda _x: np.sqrt(- _x / 2 + 5/2),
        "y = f_1(x) (2 half)": lambda _x: -np.sqrt(- _x / 2 + 5/2),
        "y = f_2(x)": lambda _x: 5/2 * (_x + 1)**2 - 1/2
    }
]

SYSTEMS_PHI = [
    {
        "x": [
            [  # From first equation
                lambda _x, _y: _x ** 2 + 3 / 4 * _y ** 2,
                lambda _x, _y: sqrt(_x - 3 / 4 * _y ** 2)
            ],
            [  # From second equation
                lambda _x, _y: (- 5 / 16 * _y**2 + _y / 8 + 3 / 16) ** (1/3)
            ]
        ],
        "y": [
            [  # From first equation
                lambda _x, _y: sqrt(-4 / 3 * _x ** 2 + 4 / 3 * _x),
            ],
            [  # From second equation
                lambda _x, _y: 8 * _x**3 + 5 / 2 * _y**2 - 3 / 2,
                lambda _x, _y: sqrt(-16 / 5 * _x**3 + 2 / 5 * _y + 3 / 5)
            ]
        ]
    },
    {
        "x": [
            [
                lambda _x, _y: -2 * _y**2 + 5,
            ],
            [
                lambda _x, _y: sqrt(2/5 * _y + 1/5) - 1
            ]
        ],
        "y": [
            [
                lambda _x, _y: sqrt(-1/2 * _x + 5/2)
            ],
            [
                lambda _x: 5/2 * (_x + 1) ** 2 - 1/2
            ]
        ]
    }
]

SYSTEMS_PHI_DERIVATIVES = [
    {
        "x": {  # dx
            "dx": [
                [
                    lambda _x, _y: 2 * _x,
                    lambda _x, _y: 1 / (2 * sqrt(_x - 3 / 4 * _y ** 2))
                ],
                [
                    lambda _x, _y: 0
                ]
            ],
            "dy": [
                [
                    lambda _x, _y: 3 / 2 * _y,
                    lambda _x, _y: - 3 * _y / (4 * sqrt(_x - 3 / 4 * _y ** 2))
                ],
                [
                    lambda _x, _y: 1/3 * (- 5 / 16 * _y**2 + _y / 8 + 3 / 16) ** (-2/3) * (-5/8 * _y + 1/8)
                ]
            ]
        },
        "y": {
            "dx": [
                [
                    lambda _x, _y: 1/2 * (-4 / 3 * _x ** 2 + 4 / 3 * _x) ** (-1/2) * (-8/3 * _x + 4/3),
                ],
                [
                    lambda _x, _y: 24 * _x**2,
                    lambda _x, _y: 1/2 * (-16 / 5 * _x**3 + 2 / 5 * _y - 3 / 5) ** (-1/2) * (-16 * 3 / 5 * _x**2)
                ]
            ],
            "dy": [
                [
                    lambda _x, _y: 0,
                ],
                [
                    lambda _x, _y: 5 * _y,
                    lambda _x, _y: 1 / (5 * sqrt(-16 / 5 * _x**3 + 2 / 5 * _y + 3 / 5))
                ]
            ]
        }
    },
    {
        "x": {
            "dx":  [
                [
                    lambda _x, _y: 0,
                ],
                [
                    lambda _x, _y: 0
                ]
            ],
            "dy":  [
                [
                    lambda _x, _y: -4 * _y,
                ],
                [
                    lambda _x, _y: 1/5 * (2/5 * _y + 1/5) ** (-1/2)
                ]
            ]
        },
        "y": {
            "dx": [
                [
                    lambda _x, _y: -1/4 * (-1/2 * _x + 5/2) ** (-1/2),
                ],
                [
                    lambda _x: 5 * (_x + 1)
                ]
            ],
            "dy": [
                [
                    lambda _x, _y: 0,
                ],
                [
                    lambda _x: 0
                ]
            ]
        }
    }
]
