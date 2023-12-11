pub fn solve(input: &str) -> i32 {
    input
        .lines()
        .map(|line| {
            let history = line
                .split_whitespace()
                .map(|x| x.parse::<i32>().unwrap())
                .collect::<Vec<_>>();
            extrapolate(&history)
        })
        .sum()
}

fn extrapolate(history: &Vec<i32>) -> i32 {
    let mut diffs = vec![history.clone()];

    let mut last = diffs.last().unwrap().clone();

    while !last.iter().all(|x| *x == last[0]) {
        last = intervals(&last);
        diffs.push(last.clone());
    }

    let mut values = Vec::new();
    values.push(last[0]);

    for it in diffs.iter().rev().skip(1) {
        values.push(values.last().unwrap() + it.last().unwrap());
    }

    values.last().unwrap().clone()
}

fn intervals(nums: &Vec<i32>) -> Vec<i32> {
    nums.windows(2).map(|x| x[1] - x[0]).collect::<Vec<_>>()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45";
        let output = solve(input);
        assert_eq!(output, 114);
    }
}
