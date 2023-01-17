import matplotlib.pyplot as plt

plt.style.use('seaborn-whitegrid')


def show_graphs(calc_dots, results, errors):
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(10, 7), sharex=True, sharey=True, tight_layout=True)

    ax1.plot(calc_dots, results, label="Результат")
    ax2.plot(calc_dots, errors, label="Ошибка")

    for ax in [ax1, ax2]:
        ax.legend()
        ax.spines['top'].set_visible(False)
        ax.spines['right'].set_visible(False)
        # ax.set_xticks(calc_dots)
        # ax.set_xscale("log")
        # ax.set_yscale("log")
        ax.set_xlabel("N")

    # ax1.set_yticks(results)
    ax1.set_title("График результата от N")
    ax1.set_ylabel("Res")

    # ax2.set_yticks(errors)
    ax2.set_title("График ошибки от N")
    ax2.set_ylabel("Err")

    plt.show()

