#!/usr/bin/env python3
from collections import deque
from random import random
from sys import argv, stdin, stdout

class Trivium:
	def __init__(self, key, iv=None):
		self.key = self._pad_to_80(self._to_bits(key))
		self.iv = self._pad_to_80(self._to_bits(iv)) if iv else self._gen_rand_iv()
		self.state = self._initial_state()
		self.counter = 0
		self._warm_up_phase()

	def _gen_rand_iv(self):
		return [int(round(random())) for _ in range(80)]

	def _initial_state(self):
		state = self.key[:]
		state += [0] * 13
		state += self.iv
		state += [0] * 112
		state += [1, 1, 1]
		return deque(state)

	def _warm_up_phase(self):
		for _ in range(4*288):
			self._gen_keystream()

	def keystream(self):
		while self.counter < 2**64:
			self.counter += 1
			yield self._gen_keystream()

	def _gen_keystream(self):
		s = self.state

		t_1 = s[65] ^ s[92]
		t_2 = s[161] ^ s[176]
		t_3 = s[242] ^ s[287]

		z_i = t_1 ^ t_2 ^ t_3

		t_1 = t_1 ^ s[90] & s[91] ^ s[170]
		t_2 = t_2 ^ s[174] & s[175] ^ s[263]
		t_3 = t_3 ^ s[285] & s[286] ^ s[68]

		s.rotate(1)
		s[0] = t_3
		s[93] = t_1
		s[177] = t_2

		return z_i

	def process(self, message):
		return self._from_bits([b ^ next(self.keystream()) for b in self._to_bits(message)])

	def _pad_to_80(self, bits):
		return bits[:80] + [0] * (80 - len(bits))

	def _to_bits(self, str):
		result = []
		for c in str:
			bits = bin(ord(c))[2:]
			bits = '00000000'[len(bits):] + bits
			result.extend([int(b) for b in bits])
		return result

	def _from_bits(self, bits):
		chars = []
		for b in range(len(bits) // 8):
			byte = bits[b*8:(b+1)*8]
			chars.append(chr(int(''.join([str(bit) for bit in byte]), 2)))
		return ''.join(chars)

def main():
	if len(argv) not in (3, 4):
		print("Use: trivium.py <key> <IV> <message to encrypt/decrypt>")
		return

	KEY = argv[1]
	IV = argv[2]
	MESSAGE = argv[3] if len(argv) == 4 else stdin.read()
	CIPHER = Trivium(KEY, IV).process(MESSAGE)

	stdout.write(CIPHER)

if __name__ == "__main__":
	main()
