pub fn solve(input: &str) -> u32 {
    input.lines().map(|line| get_calibration_value(line)).sum()
}

fn get_calibration_value(s: &str) -> u32 {
    let digits = s.chars().filter(|c| c.is_digit(10)).collect::<Vec<_>>();
    let first = digits.first().unwrap();
    let last = digits.last().unwrap();
    format!("{}{}", first, last).parse::<u32>().unwrap()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet";
        let output = solve(input);
        assert_eq!(output, 142);
    }
}
