pub fn solve(input: &str) -> u32 {
    get_all_part_numbers(input).iter().sum()
}

fn get_all_part_numbers(s: &str) -> Vec<u32> {
    let mut part_numbers = Vec::new();

    let mut lines = s.lines().collect::<Vec<_>>();

    let empty_line = ".".repeat(lines[0].len());
    lines.insert(0, &empty_line);
    lines.push(&empty_line);

    for (i, line) in lines.iter().enumerate() {
        let mut start: Option<usize> = None;

        let line = pad(line);

        for (j, c) in line.chars().enumerate() {
            if c.is_ascii_digit() {
                if start.is_none() {
                    start = Some(j);
                }
                continue;
            }

            if start.is_some() {
                let num_start = start.unwrap();
                let num_end: usize = j - 1;

                start = None;

                // check if num is a part number
                let prev_char = line.chars().nth(num_start - 1).unwrap();
                if is_symbol(prev_char) {
                    part_numbers.push(line[num_start..=num_end].parse::<u32>().unwrap());
                    continue;
                }

                let next_char = line.chars().nth(num_end + 1).unwrap();
                if is_symbol(next_char) {
                    part_numbers.push(line[num_start..=num_end].parse::<u32>().unwrap());
                    continue;
                }

                let prev_line = pad(lines[i - 1]);
                for k in num_start - 1..=num_end + 1 {
                    let c = prev_line.chars().nth(k).unwrap();
                    if is_symbol(c) {
                        part_numbers.push(line[num_start..=num_end].parse::<u32>().unwrap());
                        break;
                    }
                }

                let next_line = pad(lines[i + 1]);
                for k in num_start - 1..=num_end + 1 {
                    let c = next_line.chars().nth(k).unwrap();
                    if is_symbol(c) {
                        part_numbers.push(line[num_start..=num_end].parse::<u32>().unwrap());
                        break;
                    }
                }
            }
        }
    }

    part_numbers
}

fn pad(s: &str) -> String {
    ".".to_owned() + s + "."
}

fn is_symbol(c: char) -> bool {
    !c.is_ascii_alphanumeric() && c != '.'
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..";
        let output = solve(input);
        assert_eq!(output, 4361);
    }
}
