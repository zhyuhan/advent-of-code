use std::{collections::HashMap, vec};

pub fn solve(input: &str) -> u64 {
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

            let unknown = vec![unknown; 5].join("?");
            let groups = vec![groups; 5]
                .iter()
                .flatten()
                .cloned()
                .collect::<Vec<_>>();

            count_arrangements(&unknown, &groups, 0, &mut HashMap::new())
        })
        .sum()
}

fn count_arrangements<'a, 'b, 'c>(
    unknown: &'a str,
    groups: &'b [usize],
    curr_group_len: usize,
    cache: &'c mut HashMap<(&'a str, &'b [usize], usize), u64>,
) -> u64 {
    if let Some(&count) = cache.get(&(unknown, groups, curr_group_len)) {
        return count;
    }

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
            '#' => count += count_arrangements(&unknown[1..], groups, curr_group_len + 1, cache),
            '.' => {
                count += if curr_group_len == 0 {
                    count_arrangements(&unknown[1..], groups, 0, cache)
                } else if !groups.is_empty() && curr_group_len == groups[0] {
                    count_arrangements(&unknown[1..], &groups[1..], 0, cache)
                } else {
                    0
                }
            }
            _ => panic!("Invalid character"),
        }
    }

    cache.insert((unknown, groups, curr_group_len), count);
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
        assert_eq!(output, 525152);
    }
}
