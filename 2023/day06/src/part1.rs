pub fn solve(input: &str) -> u32 {
    let lines = input
        .lines()
        .map(|line| line.split_whitespace().collect::<Vec<_>>());

    let times = lines
        .clone()
        .nth(0)
        .unwrap()
        .iter()
        .skip(1)
        .map(|s| s.parse::<u32>().unwrap())
        .collect::<Vec<_>>();
    let distances = lines
        .clone()
        .nth(1)
        .unwrap()
        .iter()
        .skip(1)
        .map(|s| s.parse::<u32>().unwrap())
        .collect::<Vec<_>>();

    (0..times.len())
        .map(|i| ways_to_beat(times[i], distances[i]))
        .product::<u32>()
}

fn ways_to_beat(time: u32, distance: u32) -> u32 {
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
        assert_eq!(output, 288);
    }
}
