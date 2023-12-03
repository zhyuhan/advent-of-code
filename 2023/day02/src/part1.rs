pub fn solve(input: &str) -> u32 {
    let max_cubes = [12, 13, 14];
    input
        .lines()
        .filter_map(|line| {
            let mut iter = line.split(": ");
            let id = iter
                .next()
                .unwrap()
                .replace("Game ", "")
                .parse::<u32>()
                .unwrap();
            let game = iter.next().unwrap();

            if check_game(game, max_cubes) {
                Some(id)
            } else {
                None
            }
        })
        .sum()
}

fn check_game(s: &str, max_cubes: [u32; 3]) -> bool {
    let rounds = s.split("; ");
    for round in rounds {
        let cubes = parse_round(round);
        for i in 0..3 {
            if cubes[i] > max_cubes[i] {
                return false;
            }
        }
    }
    true
}

fn parse_round(s: &str) -> Vec<u32> {
    let mut round = vec![0; 3];
    for token in s.split(", ") {
        let mut iter = token.split_whitespace();
        let count = iter.next().unwrap().parse::<u32>().unwrap();
        let color = iter.next().unwrap();
        let color_index = match color {
            "red" => 0,
            "green" => 1,
            "blue" => 2,
            _ => panic!("Invalid color"),
        };
        round[color_index] = count;
    }
    round
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green";
        let output = solve(input);
        assert_eq!(output, 8);
    }
}
