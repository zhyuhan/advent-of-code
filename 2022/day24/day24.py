from collections import deque
from typing import Literal

Coord = tuple[int, int]
Direction = Literal[1, 2, 3, 4]
Blizzard = tuple[Coord, Direction]

blizzards: list[Blizzard] = []

width = 0
height = 0

with open("./input.in") as f:
    while line := f.readline().strip():
        if line.endswith("##") or line.startswith("##"):
            continue

        width = len(line) - 2
        for i in range(1, len(line) - 1):
            coord: Coord = (i - 1, height)
            if line[i] == ">":
                blizzards.append((coord, 1))
            elif line[i] == "v":
                blizzards.append((coord, 2))
            elif line[i] == "<":
                blizzards.append((coord, 3))
            elif line[i] == "^":
                blizzards.append((coord, 4))
        height += 1


def blizzard_at_tick(blizzard: Blizzard, tick: int) -> Blizzard:
    coord, dir = blizzard
    x, y = coord
    if dir == 1:
        x = (x + tick) % width
    elif dir == 2:
        y = (y + tick) % height
    elif dir == 3:
        x = (x - tick) % width
    elif dir == 4:
        y = (y - tick) % height
    return ((x, y), dir)


def has_blizzard(coord: Coord, tick: int) -> bool:
    return any(
        map(
            lambda b: b[0] == coord, map(lambda b: blizzard_at_tick(b, tick), blizzards)
        )
    )


def steps_to_end(start: Coord, end: Coord, start_tick: int = 0) -> int:
    queue = deque[tuple[Coord, int]]()
    visited = set[tuple[Coord, int]]()

    queue.append((start, start_tick))
    visited.add((start, start_tick))

    while len(queue) > 0:
        coord, tick = queue.popleft()
        if coord == end:
            return tick + 1

        next_moves: list[Coord] = [
            (coord[0] + 1, coord[1]),  # right
            (coord[0], coord[1] + 1),  # down
            (coord[0] - 1, coord[1]),  # left
            (coord[0], coord[1] - 1),  # up
            coord,  # wait
        ]
        for move in next_moves:
            if (
                move != start
                and move != end
                and (
                    move[0] < 0 or move[0] >= width or move[1] < 0 or move[1] >= height
                )
            ):
                continue
            if (move, tick + 1) in visited or has_blizzard(move, tick + 1):
                continue

            queue.append((move, tick + 1))
            visited.add((move, tick + 1))

    return -1


# part 1
start_to_end = steps_to_end((0, -1), (width - 1, height - 1))
print(start_to_end)

# part 2
end_to_start = steps_to_end((width - 1, height), (0, 0), start_to_end)
start_to_end_again = steps_to_end((0, -1), (width - 1, height - 1), end_to_start)
print(start_to_end_again)
