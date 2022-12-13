from functools import cmp_to_key
from typing import Literal

Packet = int | list["Packet"]


def parse_packet_string(s: str) -> Packet:
    stack = []
    curr_value = ""

    for c in s:
        if c == "[":
            stack.append([])
        elif c == "]":
            p = stack.pop()

            if curr_value:
                p.append(int(curr_value))
                curr_value = ""

            if len(stack) == 0:
                return p

            stack[-1].append(p)
        elif c == ",":
            if curr_value:
                stack[-1].append(int(curr_value))
                curr_value = ""
        else:
            curr_value += c

    return []


def cmp_packets(a: Packet, b: Packet) -> Literal[-1, 0, 1]:
    """
    -1: right order
    0: no decision
    1: wrong order
    """
    if isinstance(a, int) and isinstance(b, int):
        if a < b:
            return -1
        elif a > b:
            return 1
        return 0
    if isinstance(a, int):
        return cmp_packets([a], b)
    if isinstance(b, int):
        return cmp_packets(a, [b])

    while len(a):
        if len(b) == 0:
            return 1
        match cmp_packets(a[0], b[0]):
            case 0:
                a = a[1:]
                b = b[1:]
            case other:
                return other
    return -1 if len(b) > 0 else 0


def parse_packets() -> list[Packet]:
    with open("./input.in", "r") as f:
        return [parse_packet_string(line) for line in f.readlines() if line != "\n"]


packets = parse_packets()

# part 1
total = 0

for i in range(0, len(packets), 2):
    if cmp_packets(packets[i], packets[i + 1]) == -1:
        total += (i + 2) // 2

print(total)

# part 2
dividers: list[Packet] = [[[2]], [[6]]]
packets += dividers

packets = sorted(packets, key=cmp_to_key(cmp_packets))

decoder_key = 1

for index, packet in enumerate(packets):
    if packet in dividers:
        decoder_key *= index + 1

print(decoder_key)
