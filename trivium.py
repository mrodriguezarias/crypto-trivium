from collections import deque
from random import random

class Trivium:
    random_iv = []

    def __init__(self, key, iv=None):
        self.key = self._fix_bits_to_len(self._to_bits(key))
        self.iv = self._iv_init(iv)
        self.state = self._initial_state()
        self.counter = 0
        self._warm_up_phase()

        # Uncomment for troubleshooting
        # self._print_key_and_iv(self.key, self.iv)

    def _iv_init(self, iv):
        if not iv:
            if Trivium.random_iv == []:
                print("Using random IV because it was empty!!")
                Trivium.random_iv = self._gen_rand_iv()
            else:
                print("Using already generated random IV because it was empty again!!")
            iv_value = Trivium.random_iv
        else:
            iv_value = self._fix_bits_to_len(self._to_bits(iv))
        return iv_value

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

    def process(self, message=None, nbits=None):
        if message is not None:
            bits = self._to_bits(message)
            nbits = nbits if nbits else len(bits)

        cipher = [(bits[i] if message else 0) ^ next(self.keystream()) for i in range(nbits)]
        return self._from_bits(self._fix_bits_to_len(cipher, nbits+7 - (nbits+7)%8))

    def _fix_bits_to_len(self, bits, length=80, left=False):
        zeroes = [0] * (length - len(bits))
        return zeroes + bits[max(0, len(bits) - length):] if left else bits[:length] + zeroes

    def _to_bits(self, bytes):
        bytes = bytes.encode("utf-8") if isinstance(bytes, str) else bytes
        result = []
        for c in bytes:
            bits = bin(c)[2:]
            bits = '00000000'[len(bits):] + bits
            result.extend([int(b) for b in bits])
        return result

    def _from_bits(self, bits):
        result = []
        for i in range(len(bits) // 8):
            byte = bits[i*8:(i+1)*8]
            b = int(''.join([str(bit) for bit in byte]), 2)
            result.append(b)
        return bytes(result)

    def _print_key_and_iv(self, key, iv):
        print("Key Used:")
        print(key)

        print("IV Used:")
        print(iv)
        print("")
        return ""