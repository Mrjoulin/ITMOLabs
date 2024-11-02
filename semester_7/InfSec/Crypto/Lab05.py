import math

e = 3
N = [
    457829717113,
    461639371789,
    463811451073
]
C1 = [
    118519640042,
    325725597818,
    449577094588,
    225738390357,
    390837010969,
    417997930307,
    186946730799,
    307353836168,
    331923022405,
    439103095463,
    415559987555,
    407104561771
]
C2 = [
    68925059719,
    320794723471,
    106708759661,
    267503416207,
    176633626568,
    370938941185,
    256010935139,
    375173961262,
    50942041502,
    13373860798,
    369523972407,
    268680126161
]
C3 = [
    360911630335,
    49077546247,
    367587011852,
    205773073385,
    166430526462,
    166130351420,
    240614091730,
    1307748376,
    289507057580,
    309981198851,
    123903944003,
    113555743553
]
C = [C1, C2, C3]

print("Input data:")
print(f'e = {e}')
print(' \t'.join(map(lambda it: f'N{it[0] + 1} = {it[1]}', enumerate(N))))
for i, c in enumerate(C):
    print(f'Cipher C{i + 1}:')
    print("\t".join(map(str, c[:6])))
    print("\t".join(map(str, c[6:])))


M0 = math.prod(N)
m = [math.prod(N[:i] + N[i+1:]) for i in range(len(N))]
n = [pow(i, -1, j) for i, j in zip(m, N)]

print("\nFound main params:")
print(f'M0 = {M0}')
print(' \t'.join(map(lambda it: f'm{it[0] + 1} = {it[1]}', enumerate(m))))
print(' \t'.join(map(lambda it: f'n{it[0] + 1} = {it[1]}', enumerate(n))))
print()


text = ''
for i in range(0, len(C1)):
    S = sum([C[j][i] * n[j] * m[j] for j in range(len(C))]) % M0
    M = round(S ** (1/e))

    code = M.to_bytes(4,'big')
    block = code.decode('cp1251')
    text += block 

    if i<=2:
        print(f"Decipher block #{i}:")
        print(f'\tS mod M0 = {S}')
        print(f'\tM = {M}')
        print(f"\tResult text: {block}")
    else:
        print(f'Decipher block #{i}: {block}')

print("\n\nResult:")
print(text)