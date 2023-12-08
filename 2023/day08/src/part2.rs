use num::integer::lcm;
use std::collections::HashMap;

pub fn solve(input: &str) -> u64 {
    let mut lines = input.lines();

    let moves = lines.next().unwrap();

    let mut map = HashMap::new();

    for line in lines.skip(1) {
        let mut iter = line.split(" = ");
        let node = iter.next().unwrap();
        let next = iter.next().unwrap();
        let neighbours = next
            .trim_start_matches('(')
            .trim_end_matches(')')
            .split(", ")
            .collect::<Vec<_>>();

        map.insert(node, (neighbours[0], neighbours[1]));
    }

    navigate(map, moves)
}

fn navigate(map: HashMap<&str, (&str, &str)>, moves: &str) -> u64 {
    let mut num_moves = 0;

    let mut curr = map
        .keys()
        .filter_map(|k| if k.ends_with("A") { Some(*k) } else { None })
        .collect::<Vec<_>>();

    let mut moves_to_end = vec![0_u64; curr.len()];

    while !has_reached_end(&moves_to_end) {
        let next_move = moves
            .chars()
            .nth(usize::try_from(num_moves).unwrap() % moves.len())
            .unwrap();

        for i in 0..curr.len() {
            if curr[i].ends_with("Z") && moves_to_end[i] == 0 {
                moves_to_end[i] = num_moves;
            }

            let next = map.get(curr[i]).unwrap();
            match next_move {
                'L' => curr[i] = next.0,
                'R' => curr[i] = next.1,
                _ => panic!("Invalid move"),
            }
        }

        num_moves += 1;
    }

    moves_to_end.iter().fold(1, |acc, x| lcm(acc, *x))
}

fn has_reached_end(nodes: &Vec<u64>) -> bool {
    nodes.iter().all(|n| *n > 0)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)";
        let output = solve(input);
        assert_eq!(output, 6);
    }
}
