use std::collections::HashMap;

pub fn solve(input: &str) -> u32 {
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

fn navigate(map: HashMap<&str, (&str, &str)>, moves: &str) -> u32 {
    let mut num_moves = 0;
    let mut curr = "AAA";

    while curr != "ZZZ" {
        let next = map.get(curr).unwrap();
        let next_move = moves
            .chars()
            .nth(usize::try_from(num_moves).unwrap() % moves.len())
            .unwrap();

        match next_move {
            'L' => curr = next.0,
            'R' => curr = next.1,
            _ => panic!("Invalid move"),
        }

        num_moves += 1;
    }

    num_moves
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)";
        let output = solve(input);
        assert_eq!(output, 2);
    }

    #[test]
    fn test_solve2() {
        let input = "LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)";
        let output = solve(input);
        assert_eq!(output, 6);
    }
}
