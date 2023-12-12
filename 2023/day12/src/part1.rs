pub fn solve(input: &str) -> u32 {
    input
        .lines()
        .map(|line| {
            let mut iter = line.split_whitespace();
            let unknown = iter.next().unwrap();
            let groups = iter
                .next()
                .unwrap()
                .split(",")
                .map(|x| x.parse::<usize>().unwrap())
                .collect::<Vec<_>>();
            count_arrangements(unknown, &groups, 0)
        })
        .sum()
}

fn count_arrangements(unknown: &str, groups: &[usize], curr_group_len: usize) -> u32 {
    if unknown.is_empty() {
        return if groups.is_empty() {
            if curr_group_len == 0 {
                1
            } else {
                0
            }
        } else if groups.len() == 1 && curr_group_len == groups[0] {
            1
        } else {
            0
        };
    }

    let mut count = 0;

    let next = unknown.chars().next().unwrap();

    let possible = match next {
        '?' => vec!['.', '#'],
        _ => vec![next],
    };

    for c in possible {
        match c {
            '#' => count += count_arrangements(&unknown[1..], groups, curr_group_len + 1),
            '.' => {
                count += if curr_group_len == 0 {
                    count_arrangements(&unknown[1..], groups, 0)
                } else if !groups.is_empty() && curr_group_len == groups[0] {
                    count_arrangements(&unknown[1..], &groups[1..], 0)
                } else {
                    0
                }
            }
            _ => panic!("Invalid character"),
        }
    }

    count
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1";
        let output = solve(input);
        assert_eq!(output, 21);
    }
}
