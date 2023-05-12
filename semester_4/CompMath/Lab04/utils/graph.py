import matplotlib.pyplot as plt
import numpy as np
import warnings

warnings.filterwarnings("ignore")
plt.style.use('seaborn-whitegrid')


def show_functions_graph(functions, dots):
    fig, ax = plt.subplots(1, 1, figsize=(10, 7), sharex=True, sharey=True, tight_layout=True)

    by_y = functions.pop("by_y") if "by_y" in functions else False

    bounds = [dots[0][0] - 2, dots[-1][0] + 2]
    x = np.linspace(bounds[0], bounds[1], max(int((bounds[1] - bounds[0]) * 10), 1000))

    ax.scatter(dots[:, 0], dots[:, 1], label='Points')

    for func_name, func in functions.items():
        if func is not None:
            ax.plot(x, func(x), label=func_name)

    ax.legend()
    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)
    ax.set_xlim(bounds)
    ax.set_title("Functions graph")
    ax.set_xlabel("X" if not by_y else "Y")
    ax.set_ylabel("Y" if not by_y else "X")

    plt.show(block=False)
