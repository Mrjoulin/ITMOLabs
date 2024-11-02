import math

N = 77027476849549 
e = 2936957 
C = [
	18937689886043,
	6667195679130,
	53238895771820,
	6189192838687,
	48623327840257,
	47264919314001,
	42510070950746,
	16878504505970,
	22744978157662,
	23644842894223
]

print("Input data:")
print(f'N = {N}\te = {e}')
print(f'Cipher:')
print("\t".join(map(str, C[:5])))
print("\t".join(map(str, C[5:])))


iters = 1000
n = math.ceil(math.sqrt(N))
w = 0

print(f"\nStart n = {n}")

for i in range(iters):
	w = n**2 - N
	# print(i, math.sqrt(w) % 1)
	if math.sqrt(w) % 1 == 0:
		print(f"Found w in {i} iterations")
		print(f"t_{i} = {n}\tw_{i} = {w}")
		break
	n += 1

	if i == iters - 1:
		print("w not found")
		exit(1)

p = n + math.sqrt(w)
q = n - math.sqrt(w)
phi_n = int((p - 1) * (q - 1))
d = pow(e, -1, phi_n)

print("Found RSA params:")
print(f'\tp\t=\t{int(p)}')
print(f'\tq\t=\t{int(q)}')
print(f'\tф(N)\t=\t{int(phi_n)}')
print(f'\td\t=\t{int(d)}\n')


text = ''
for i, c in enumerate(C):
    mes = pow(c, d, N)
    code = mes.to_bytes(4, byteorder='big')
    block = code.decode('cp1251')
    text += block 
    print(f'Decipher block #{i}: {block}')

print("\n\nResult:")
print(text)