def snafu_to_int(snafu: str) -> int:
    num = 0
    for i, c in enumerate(reversed(snafu)):
        if c == "-":
            num -= 5**i
        elif c == "=":
            num -= 2 * 5**i
        else:
            num += int(c) * 5**i
    return num


def int_to_snafu(num: int) -> str:
    snafu_digits = {
        0: "0",
        1: "1",
        2: "2",
        -1: "-",
        -2: "=",
    }
    snafu = ""
    while num != 0:
        digit = 0
        if num % 5 < 3:
            digit = num % 5
        else:
            digit = num % 5 - 5
            num += 5

        snafu += snafu_digits[digit]
        num //= 5
    return snafu[::-1]


total_fuel = 0

with open("./input.in") as f:
    while line := f.readline().strip():
        total_fuel += snafu_to_int(line)

print(int_to_snafu(total_fuel))
