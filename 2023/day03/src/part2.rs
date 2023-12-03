pub fn solve(input: &str) -> u32 {
    get_all_gear_ratios(input)
        .iter()
        .filter_map(|gear_ratio| {
            if let Some(right) = gear_ratio.right {
                Some(gear_ratio.left * right)
            } else {
                None
            }
        })
        .sum()
}

struct GearRatio {
    symbol_pos: [usize; 2],
    left: u32,
    right: Option<u32>,
}

fn get_all_gear_ratios(s: &str) -> Vec<GearRatio> {
    let mut gear_ratios: Vec<GearRatio> = Vec::new();

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
                if is_gear_symbol(prev_char) {
                    add_to_gear_ratio(
                        &mut gear_ratios,
                        i,
                        num_start - 1,
                        line[num_start..=num_end].parse::<u32>().unwrap(),
                    );
                    continue;
                }

                let next_char = line.chars().nth(num_end + 1).unwrap();
                if is_gear_symbol(next_char) {
                    add_to_gear_ratio(
                        &mut gear_ratios,
                        i,
                        num_end + 1,
                        line[num_start..=num_end].parse::<u32>().unwrap(),
                    );
                    continue;
                }

                let prev_line = pad(lines[i - 1]);
                for k in num_start - 1..=num_end + 1 {
                    let c = prev_line.chars().nth(k).unwrap();
                    if is_gear_symbol(c) {
                        add_to_gear_ratio(
                            &mut gear_ratios,
                            i - 1,
                            k,
                            line[num_start..=num_end].parse::<u32>().unwrap(),
                        );
                        break;
                    }
                }

                let next_line = pad(lines[i + 1]);
                for k in num_start - 1..=num_end + 1 {
                    let c = next_line.chars().nth(k).unwrap();
                    if is_gear_symbol(c) {
                        add_to_gear_ratio(
                            &mut gear_ratios,
                            i + 1,
                            k,
                            line[num_start..=num_end].parse::<u32>().unwrap(),
                        );
                        break;
                    }
                }
            }
        }
    }

    gear_ratios
}

fn add_to_gear_ratio(gear_ratios: &mut Vec<GearRatio>, row: usize, col: usize, num: u32) {
    if let Some(index) = gear_ratios
        .iter()
        .position(|x| x.symbol_pos[0] == row && x.symbol_pos[1] == col)
    {
        gear_ratios[index].right = Some(num);
    } else {
        gear_ratios.push(GearRatio {
            symbol_pos: [row, col],
            left: num,
            right: None,
        });
    }
}

fn pad(s: &str) -> String {
    ".".to_owned() + s + "."
}

fn is_gear_symbol(c: char) -> bool {
    c == '*'
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
        assert_eq!(output, 467835);
    }
}
