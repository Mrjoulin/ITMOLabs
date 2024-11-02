import random
import os
from string import printable

# Consts
rus = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя«»—"
alph = rus + printable

REG_LEN = 80

LSR1 = [random.randint(0, 1) for _ in range(REG_LEN)]
LSR2 = [random.randint(0, 1) for _ in range(REG_LEN)]


def prepare_message(m):
    try:
        res = [alph.index(let) for let in m]
    except ValueError as e:
        print("Unacceptable letter: " + str(e))
        exit(1)

    return res

def get_message(indexes):
    mes = "".join([alph[ind] for ind in indexes])
    return mes


def roll_registers(r1, r2):
	r1 = [r1[-1]] + r1[:-2]
	r2 = [r2[-1]] + r2[:-2]
	return r1, r2


def to_bin(x):
	x_b = bin(x)[2:]
	return '0' * (8 - len(x_b)) + x_b


def encode(text_ints, lsr1, lsr2):
	gammas = []
	out = []

	for let in text_ints:
		let_out = []
		for bit in to_bin(let):
			if len(gammas) <= 1:
				gamma = lsr1[-1] ^ lsr2[-1]
				gammas.append(gamma)
			else:
				gamma = lsr1[-1] ^ lsr2[-1] ^ (gammas[0] & gammas[1])
				gammas = [gammas[1], gamma]

			roll_registers(lsr1, lsr2)

			let_out.append(int(bit) ^ gamma)

		let_code = int("".join(map(str, let_out)), 2)
		out.append(let_code)

	return out


def decode(encoded_message, lsr1, lsr2):
	decoded = encode(encoded_message, lsr1, lsr2)
	return get_message(decoded)


filename = input("Input filename: ")
if not os.path.exists(filename):
     print("File not exists")
     exit(1)

with open(filename, "r") as f:
     message = f.read()


print("Generated LSR1:", "".join(map(str, LSR1)))
print("Generated LSR2:", "".join(map(str, LSR2)))


message_ints = prepare_message(message)

encoded_message = encode(message_ints, LSR1.copy(), LSR2.copy())

print("\n\nEncoded:")
print("\t".join([f"{i:x}" for i in encoded_message]))


decoded_message = decode(encoded_message, LSR1.copy(), LSR2.copy())


print("\n\nDecoded:")
print(decoded_message)


