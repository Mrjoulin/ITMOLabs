from equations import *


def check_system_convergence(system_ind, approx):
    phi = SYSTEMS_PHI[system_ind]

    suitable_systems = []

    for i in range(2):
        for x_eq_ind in range(len(phi["x"][i])):
            for y_eq_ind in range(len(phi["y"][1 - i])):

                res = check_conditions(system_ind, i, x_eq_ind, y_eq_ind, approx)

                if res:
                    # Return suitable phi
                    suitable_systems.append(
                        [phi["x"][i][x_eq_ind], phi["y"][1 - i][y_eq_ind]]
                    )

    if suitable_systems:
        print("Find %d suitable systems" % len(suitable_systems))

    return suitable_systems


def check_conditions(system_ind, selected_x_ind, x_eq_ind, y_eq_ind, approx):
    phi_1_x = SYSTEMS_PHI_DERIVATIVES[system_ind]["x"]["dx"][selected_x_ind][x_eq_ind]
    phi_1_y = SYSTEMS_PHI_DERIVATIVES[system_ind]["x"]["dy"][selected_x_ind][x_eq_ind]
    phi_2_x = SYSTEMS_PHI_DERIVATIVES[system_ind]["y"]["dx"][1 - selected_x_ind][y_eq_ind]
    phi_2_y = SYSTEMS_PHI_DERIVATIVES[system_ind]["y"]["dy"][1 - selected_x_ind][y_eq_ind]

    phis = [[phi_1_x, phi_1_y], [phi_2_x, phi_2_y]]
    offset = 0.25

    for i in range(2):
        maxes = []
        for j, phi in enumerate(phis[i]):
            cur_max = 0
            cur_x, cur_y = approx[0] - offset, approx[1] - offset
            while cur_x <= approx[0] + offset:
                while cur_y <= approx[1] + offset:
                    try:
                        cur_max = max(cur_max, abs(phi(cur_x, cur_y)))
                    except Exception:
                        return False

                    cur_y += 0.1
                cur_x += 0.1

            maxes.append(cur_max)

            if cur_max >= 1:
                # print(system_ind, selected_x_ind, x_eq_ind, y_eq_ind, "-", i, j, "-", cur_max)
                return False

        if sum(maxes) >= 1:
            # print(system_ind, selected_x_ind, x_eq_ind, y_eq_ind, "-", i, "-", maxes)
            return False

    return True
