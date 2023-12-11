use std::collections::{HashMap, VecDeque};

pub fn solve(input: &str) -> u32 {
    let map = input
        .lines()
        .map(|line| line.chars().collect::<Vec<_>>())
        .collect::<Vec<_>>();

    let mut start = (0, 0);

    for y in 0..map.len() {
        for x in 0..map[0].len() {
            if map[y][x] == 'S' {
                start = (x, y);
                break;
            }
        }
    }

    let mut queue = VecDeque::new();
    let mut visited = vec![vec![false; map[0].len()]; map.len()];
    visited[start.1][start.0] = true;

    let mut path = HashMap::new();

    for dir in vec![
        Direction::Left,
        Direction::Right,
        Direction::Up,
        Direction::Down,
    ] {
        if start.0 == 0 && dir == Direction::Left {
            continue;
        }
        if start.1 == 0 && dir == Direction::Up {
            continue;
        }

        let (x, y) = match dir {
            Direction::Left => (start.0 - 1, start.1),
            Direction::Right => (start.0 + 1, start.1),
            Direction::Up => (start.0, start.1 - 1),
            Direction::Down => (start.0, start.1 + 1),
        };

        if is_reachable(&map, x, y, dir) {
            queue.push_back((x, y, dir, 1));
            path.insert((x, y), dir);
        }
    }

    let mut max_steps = usize::MIN;

    while let Some((x, y, dir, steps)) = queue.pop_front() {
        if visited[y][x] {
            continue;
        }

        visited[y][x] = true;
        max_steps = max_steps.max(steps);

        let (dx, dy, dir) = next(map[y][x], dir);
        let (x, y) = ((x as i32 + dx) as usize, (y as i32 + dy) as usize);

        if is_reachable(&map, x, y, dir) {
            queue.push_back((x, y, dir, steps + 1));
            path.insert((x, y), dir);
        }
    }

    let mut num_within_loop = 0;

    for y in 0..map.len() {
        let mut is_in = false;
        let mut last = None;

        for x in 0..map[0].len() {
            if visited[y][x] {
                let curr = map[y][x];
                if curr == '-' {
                    continue;
                }

                if curr == '|' {
                    is_in = !is_in;
                } else if let Some(l) = last {
                    if (curr == '7' && l == 'L') || (curr == 'J' && l == 'F') {
                        is_in = !is_in;
                    }
                }

                last = Some(curr);
                continue;
            }

            if is_in {
                num_within_loop += 1;
            }
        }
    }

    num_within_loop
}

fn is_reachable(map: &Vec<Vec<char>>, x: usize, y: usize, dir: Direction) -> bool {
    if x >= map[0].len() || y >= map.len() {
        return false;
    }

    match map[y][x] {
        '.' => false,
        '|' => match dir {
            Direction::Up | Direction::Down => true,
            _ => false,
        },
        '-' => match dir {
            Direction::Left | Direction::Right => true,
            _ => false,
        },
        'L' => match dir {
            Direction::Left | Direction::Down => true,
            _ => false,
        },
        'J' => match dir {
            Direction::Right | Direction::Down => true,
            _ => false,
        },
        '7' => match dir {
            Direction::Right | Direction::Up => true,
            _ => false,
        },
        'F' => match dir {
            Direction::Left | Direction::Up => true,
            _ => false,
        },
        _ => panic!("Invalid move"),
    }
}

#[derive(Debug, Clone, Copy, Eq, PartialEq)]
enum Direction {
    Up,
    Down,
    Left,
    Right,
}

fn next(c: char, dir: Direction) -> (i32, i32, Direction) {
    match c {
        '|' => match dir {
            Direction::Up => (0, -1, Direction::Up),
            Direction::Down => (0, 1, Direction::Down),
            _ => panic!("Invalid direction"),
        },
        '-' => match dir {
            Direction::Left => (-1, 0, Direction::Left),
            Direction::Right => (1, 0, Direction::Right),
            _ => panic!("Invalid direction"),
        },
        'L' => match dir {
            Direction::Left => (0, -1, Direction::Up),
            Direction::Down => (1, 0, Direction::Right),
            _ => panic!("Invalid direction"),
        },
        'J' => match dir {
            Direction::Right => (0, -1, Direction::Up),
            Direction::Down => (-1, 0, Direction::Left),
            _ => panic!("Invalid direction"),
        },
        '7' => match dir {
            Direction::Right => (0, 1, Direction::Down),
            Direction::Up => (-1, 0, Direction::Left),
            _ => panic!("Invalid direction"),
        },
        'F' => match dir {
            Direction::Left => (0, 1, Direction::Down),
            Direction::Up => (1, 0, Direction::Right),
            _ => panic!("Invalid direction"),
        },
        _ => panic!("Invalid move"),
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........";
        let output = solve(input);
        assert_eq!(output, 4);
    }

    #[test]
    fn test_solve2() {
        let input = ".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...";
        let output = solve(input);
        assert_eq!(output, 8);
    }
}
