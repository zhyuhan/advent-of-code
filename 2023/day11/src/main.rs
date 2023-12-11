fn main() {
    let input = include_str!("../input.in");
    println!("Part 1: {}", solve(input, 2));
    println!("Part 2: {}", solve(input, 1000000));
}

fn solve(input: &str, expansion_rate: u64) -> u64 {
    let map = input
        .lines()
        .map(|line| line.chars().collect::<Vec<_>>())
        .collect::<Vec<_>>();

    let empty_rows = map
        .iter()
        .enumerate()
        .filter_map(|(i, row)| {
            if row.iter().all(|c| *c == '.') {
                Some(i)
            } else {
                None
            }
        })
        .collect::<Vec<_>>();
    let empty_cols = (0..map[0].len())
        .filter_map(|i| {
            if map.iter().all(|row| row[i] == '.') {
                Some(i)
            } else {
                None
            }
        })
        .collect::<Vec<_>>();

    let galaxies = map
        .iter()
        .enumerate()
        .flat_map(|(y, row)| {
            row.iter()
                .enumerate()
                .filter_map(move |(x, c)| if *c == '#' { Some((x, y)) } else { None })
        })
        .collect::<Vec<_>>();

    let mut totat_dist = 0;

    for (x1, y1) in galaxies.iter() {
        for (x2, y2) in galaxies.iter() {
            if x1 == x2 && y1 == y2 {
                continue;
            }

            let mut distance = (x2.abs_diff(*x1) + y2.abs_diff(*y1)) as u64;

            for r in empty_rows.iter() {
                if *r > *y1.min(y2) && *r < *y1.max(y2) {
                    distance += expansion_rate - 1;
                }
            }
            for c in empty_cols.iter() {
                if *c > *x1.min(x2) && *c < *x1.max(x2) {
                    distance += expansion_rate - 1;
                }
            }

            totat_dist += distance;
        }
    }

    totat_dist / 2
}

#[cfg(test)]
mod tests {
    use super::*;

    const INPUT: &str = "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....";

    #[test]
    fn test_solve_expand_by_2() {
        let output = solve(INPUT, 2);
        assert_eq!(output, 374);
    }

    #[test]
    fn test_solve_expand_by_10() {
        let output = solve(INPUT, 10);
        assert_eq!(output, 1030);
    }

    #[test]
    fn test_solve_expand_by_100() {
        let output = solve(INPUT, 100);
        assert_eq!(output, 8410);
    }
}
