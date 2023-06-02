from itertools import permutations
from subprocess import PIPE, run
import random

"""
    Tests generator for task F
"""

FILENAME = "F"  # F.cpp -> F (executable)

N = int(input("Num tests: "))
auto = bool(int(input("Auto (1 or 0): ")))

for test_num in range(N):
    print("\nTest", test_num + 1, "\n")

    lines = []

    while not auto:
        try:
            lines.append(input().replace(" ", ""))
        except EOFError:
            break

    if not lines:
        s_len = 6
        s = "".join([str(random.randint(0, 9)) for _ in range(s_len)])
        
        if s[0] == '0':
            s = str(random.randint(1, 9)) + s
            s_len += 1

        for i in range(s_len):
            lines.append(s + s[:i])

        print("\n".join(lines))

    print("Correct:")

    # Just check all permutations and take max
    max_per = 0
    max_ind = 0
    perm = list(permutations(lines))

    for i, cur in enumerate(perm):
        num = int("".join(cur))
        if num > max_per:
            max_per = num
            max_ind = i

    print(" ".join(perm[max_ind]))
    print("Your:")

    with open("tmp.txt", "w") as f:
        f.write("\n".join(lines))

    result = run(["g++", "%s.cpp" % FILENAME, "-o", FILENAME], stdout=PIPE, stderr=PIPE, universal_newlines=True)
    result = run(['./%s' % FILENAME], stdin=open('tmp.txt'), stdout=PIPE, stderr=PIPE, universal_newlines=True)

    out = result.stdout
    print(out)

    try:
        res = int(out.replace(" ", ""))
        print("Same:",  res == max_per, "diff:", max_per - res)

        if (res != max_per):
            break
    except Exception:
        print("Imposible to compare")
        break
