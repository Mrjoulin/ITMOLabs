import numpy as np
import os
from string import printable

# Consts
rus = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя«»—"
alph = rus + printable


Sbox = np.array([
    0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
    0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
    0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
    0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
    0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
    0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
    0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
    0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
    0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
    0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
    0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
    0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
    0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
    0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
    0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
    0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16
])

SboxInv = np.array([
    0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb,
    0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb,
    0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e,
    0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25,
    0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92,
    0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84,
    0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06,
    0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b,
    0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73,
    0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e,
    0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b,
    0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4,
    0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f,
    0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef,
    0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61,
    0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d
])


mixcol_matrix = np.array([
    [2, 3, 1, 1],
    [1, 2, 3, 1],
    [1, 1, 2, 3],
    [3, 1, 1, 2]
])

mixcol_matrix_inv = np.array([
    [14, 11, 13, 9],
    [9, 14, 11, 13],
    [13, 9, 14, 11],
    [11, 13, 9, 14]
])


key_lenght_bytes = [16, 24, 32]

# Map max(key len, block len) to number of rounds
rounds_map = {
    4: 10,
    6: 12,
    8: 14
}

shifts_by_block_len = {
    4: [0, 1, 2, 3],
    6: [0, 1, 2, 3],
    8: [0, 1, 3, 4]
}

DEBUG_MODE = False

# Process 

# Help methods

def assert_block_size(block_size: str):
    if not block_size.isdigit():
        print("Block size should be correct integer")
        exit(1)

    block_size = int(block_size)
    if block_size not in rounds_map and block_size // 32 not in rounds_map:
        print("Incorrect block size")
        exit(1)
    elif block_size > 8:
        block_size = block_size // 32

    return block_size


def prepare_message(m, block_len: int):
    res = []
    try:
        for let in m:
            if DEBUG_MODE:
                print(let, end='')
            res.append(alph.index(let) + 1)
    except ValueError as e:
        print("Unacceptable letter: " + str(e))
        exit(1)

    remain = block_len * 4 - (len(res) % (block_len * 4))
    if remain < block_len * 4:
        res += [0] * remain
    return np.array(res).reshape((4, -1), order='F')


def get_message(indexes):
    mes = ""
    for ind in indexes:
        if ind == 0:
            break
        mes += alph[ind - 1]
    return mes


def print_matrix(m):
    print("\n".join(["\t".join([f"{i:x}" for i in line]) for line in m]))


def gmul(a, b):
    p = 0
    for c in range(8):
        if b & 1:
            p ^= a
        a <<= 1
        if a & 0x100:
            a ^= 0x11b
        b >>= 1
    return p

# Key Scheduling

def getRcon(num_rounds: int):
    rcon = np.zeros((num_rounds, 4), dtype='int64')
    rcon[0, 0] = 1
    for i in range(1, num_rounds):
        if rcon[i - 1, 0] >= 0x80:
            rcon[i, 0] = (rcon[i - 1, 0] * 2) ^ 0x11b
        else:
            rcon[i, 0] = rcon[i - 1, 0] * 2
    return rcon


def KeySchedule(key, num_rounds: int):
    n = key.shape[1]

    res = np.zeros((n * num_rounds, 4), dtype='int64')
    res[:n, :] = key.T
    rcon = getRcon(num_rounds)

    if DEBUG_MODE:
        print("Rcon matrix:")
        print_matrix(rcon)

    for i in range(n, n * num_rounds):
        if i % n == 0:
            res[i] = res[i - n] ^ SubBytes(np.roll(res[i - 1], -1)) ^ rcon[i // n - 1]
        elif n > 6 and i % n == 4:
            res[i] = res[i - n] ^ SubBytes(res[i - 1])
        else:
            res[i] = res[i - n] ^ res[i - 1]

    return res.T


# Round steps

def SubSelection(sub_array, block_len, cur_step):
    return sub_array[:, block_len * cur_step: block_len * (cur_step + 1)]


def SubBytes(state, sbox=Sbox):
    return sbox[state]


def ShiftRows(state, inv: bool = False):
    n = state.shape[0]
    sign = int(inv) * 2 - 1
    return np.array([np.roll(state[i], sign * shifts_by_block_len[n][i]) for i in range(n)])


def MixColumns(state, matrix):
    res = np.zeros(state.shape, dtype='int64')
    for col in range(state.shape[1]):
        for cur_col in range(state.shape[0]):
            for row in range(state.shape[0]):
                res[cur_col][col] ^= gmul(matrix[cur_col][row], state[row][col])
    return res


def process_encode_round(state, cicle_key, final_round: bool = False):
    state = SubBytes(state, Sbox)
    if DEBUG_MODE:
        print("[ENCODE] After Sub Bytes")
        print_matrix(state)

    state = ShiftRows(state)
    if DEBUG_MODE:
        print("[ENCODE] After Shift Rows")
        print_matrix(state)

    if not final_round:
        state = MixColumns(state, mixcol_matrix)
        if DEBUG_MODE:
            print("[ENCODE] After Mix Columns")
            print_matrix(state)

    state = state ^ cicle_key

    if DEBUG_MODE:
        print("[ENCODE] Round result")
        print_matrix(state)

    return state


def encode(message: str, key: str, block_len: int):
    num_rounds = rounds_map[block_len]
    message_mat = prepare_message(message, block_len)

    if DEBUG_MODE:
        print("\n\nInput matrix:")
        print_matrix(message_mat)

    key_mat = prepare_message(key, len(key) // 4)
    key_sched = KeySchedule(key_mat, num_rounds)

    result = np.zeros(message_mat.shape, dtype='int64')

    for cur_block in range(message_mat.shape[1] // block_len):
        state = SubSelection(message_mat, block_len, cur_block)

        for cur_round in range(num_rounds):
            state = process_encode_round(
                state, 
                SubSelection(key_sched, block_len, cur_round),
                final_round=cur_round == num_rounds - 1
            )

        result[:, block_len * cur_block:block_len * (cur_block + 1)] = state

    return result


def process_decode_round(state, cicle_key, first_round: bool = False):
    state = state ^ cicle_key

    if DEBUG_MODE:
        print("[DECODE] After Adding Round Key")
        print_matrix(state)

    if not first_round:
        state = MixColumns(state, mixcol_matrix_inv)
        if DEBUG_MODE:
            print("[DECODE] After Mix Columns")
            print_matrix(state)


    state = ShiftRows(state, inv=True)
    if DEBUG_MODE:
        print("[DECODE] After Shift Rows")
        print_matrix(state)

    state = SubBytes(state, SboxInv)
    if DEBUG_MODE:
        print("[DECODE] After SubBytes, round result")
        print_matrix(state)

    return state


def decode(encoded_message, key: str, block_len: int):
    num_rounds = rounds_map[block_len]

    key_mat = prepare_message(key, len(key) // 4)
    key_sched = KeySchedule(key_mat, num_rounds)

    result = np.zeros(encoded_message.shape, dtype='int64')

    for cur_block in range(encoded_message.shape[1] // block_len):
        state = SubSelection(encoded_message, block_len, cur_block)

        for cur_round in range(num_rounds):
            state = process_decode_round(
                state, 
                SubSelection(key_sched, block_len, num_rounds - cur_round - 1),
                first_round=cur_round == 0
            )

        result[:, block_len * cur_block:block_len * (cur_block + 1)] = state


    if DEBUG_MODE:
        print("\n\nResult matrix:")
        print_matrix(result)
    result = result.reshape(-1, order='F')

    return get_message(result)

# Input

block_size = assert_block_size(input("Input block size (bits or words): "))
key = input("Input key: ")

if len(key) not in key_lenght_bytes:
    print("Incorrect key lenght")
    exit(1)

filename = input("Input filename: ")
if not os.path.exists(filename):
     print("File not exists")
     exit(1)

with open(filename, "r") as f:
     message = f.read()


encoded_message = encode(message, key, block_size)

print("\n\nEncoded:")
print_matrix(encoded_message)


decoded_message = decode(encoded_message, key, block_size)
print("\n\nDecoded:")
print(decoded_message)

