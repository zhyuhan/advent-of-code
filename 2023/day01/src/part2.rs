use fancy_regex::Regex;

static DIGIT_PATTERN: &str = r"(?=(one|two|three|four|five|six|seven|eight|nine|zero|\d))";

pub fn solve(input: &str) -> u32 {
    input.lines().map(|line| get_calibration_value(line)).sum()
}

fn get_calibration_value(s: &str) -> u32 {
    let re = Regex::new(DIGIT_PATTERN).unwrap();

    let digits: Vec<u32> = re
        .captures_iter(s)
        .map(|m| parse_digit_str(m.unwrap().get(1).unwrap().as_str()))
        .collect();

    let first = digits.first().unwrap();
    let last = digits.last().unwrap();
    format!("{}{}", first, last).parse::<u32>().unwrap()
}

fn parse_digit_str(s: &str) -> u32 {
    match s {
        "one" => 1,
        "two" => 2,
        "three" => 3,
        "four" => 4,
        "five" => 5,
        "six" => 6,
        "seven" => 7,
        "eight" => 8,
        "nine" => 9,
        _ => s.parse::<u32>().unwrap(),
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen";
        let output = solve(input);
        assert_eq!(output, 281);
    }
}
