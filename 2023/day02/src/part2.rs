pub fn solve(input: &str) -> u32 {
    input
        .lines()
        .map(|line| {
            let game = line.split(": ").nth(1).unwrap();
            get_game_power(game)
        })
        .sum()
}

fn get_game_power(s: &str) -> u32 {
    let mut max_cubes = [0; 3];

    let rounds = s.split("; ");
    for round in rounds {
        let cubes = parse_round(round);
        for i in 0..3 {
            max_cubes[i] = max_cubes[i].max(cubes[i]);
        }
    }

    max_cubes.iter().product()
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
        assert_eq!(output, 2286);
    }
}
