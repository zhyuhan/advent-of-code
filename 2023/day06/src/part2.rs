pub fn solve(input: &str) -> u64 {
    let lines = input
        .lines()
        .map(|line| line.split_whitespace().collect::<Vec<_>>());

    let time = lines
        .clone()
        .nth(0)
        .unwrap()
        .iter()
        .skip(1)
        .fold("".to_owned(), |acc, s| acc + s)
        .parse::<u64>()
        .unwrap();
    let distance = lines
        .clone()
        .nth(1)
        .unwrap()
        .iter()
        .skip(1)
        .fold("".to_owned(), |acc, s| acc + s)
        .parse::<u64>()
        .unwrap();

    ways_to_beat(time, distance)
}

fn ways_to_beat(time: u64, distance: u64) -> u64 {
    let mut ways = 0;

    for t in 0..time {
        let time_left = time - t;
        let speed = t;

        if speed * time_left > distance {
            ways += 1;
        }
    }

    ways
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "Time:      7  15   30
Distance:  9  40  200";
        let output = solve(input);
        assert_eq!(output, 71503);
    }
}
