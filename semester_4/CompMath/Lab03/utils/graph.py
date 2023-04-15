import matplotlib.pyplot as plt
import numpy as np
import warnings

warnings.filterwarnings("ignore")
plt.style.use('seaborn-whitegrid')


def show_functions_graph(functions):
    fig, ax = plt.subplots(1, 1, figsize=(10, 7), sharex=True, sharey=True, tight_layout=True)

    by_y = functions.pop("by_y") if "by_y" in functions else False

    bounds = [-1e2, 1e2]
    for func in functions.values():
        for i in range(2):
            cur = 0
            try:
                while abs(func(cur)) <= 1e2 and abs(cur) <= 1e2:
                    cur += i * 2 - 1
            except Exception:
                cur -= i * 2 - 1
            bounds[i] = max(bounds[i], cur) if i == 0 else min(bounds[i], cur)

    x = np.linspace(bounds[0], bounds[1], max(int((bounds[1] - bounds[0]) * 10), 1000))

    for func_name, func in functions.items():
        ax.plot(x, func(x), label=func_name)

    ax.legend()
    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)
    ax.set_xlim(bounds)
    ax.set_title("Function graph")
    ax.set_xlabel("X" if not by_y else "Y")
    ax.set_ylabel("Y" if not by_y else "X")

    plt.show(block=False)
