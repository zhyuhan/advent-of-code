pub fn solve(input: &str) -> u64 {
    let lines = input.lines();

    let mut seeds = lines
        .clone()
        .nth(0)
        .unwrap()
        .split(": ")
        .nth(1)
        .unwrap()
        .split_whitespace()
        .filter_map(|num| num.parse::<u64>().ok())
        .collect::<Vec<_>>();

    let mut map_lines = lines.clone().skip(2);
    let mut is_mapped = vec![false; seeds.len()];

    while let Some(line) = map_lines.next() {
        if line == "" {
            continue;
        }
        if line.ends_with("map:") {
            for i in 0..seeds.len() {
                is_mapped[i] = false;
            }
            continue;
        }

        let nums = line.split_whitespace().collect::<Vec<&str>>();
        let dest = nums[0].parse::<u64>().unwrap();
        let src = nums[1].parse::<u64>().unwrap();
        let len = nums[2].parse::<u64>().unwrap();

        for i in 0..seeds.len() {
            if is_mapped[i] {
                continue;
            }
            if seeds[i] >= src && seeds[i] < src + len {
                seeds[i] = dest + (seeds[i] - src);
                is_mapped[i] = true;
            }
        }
    }

    seeds.iter().min().unwrap().clone()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4";
        let output = solve(input);
        assert_eq!(output, 35);
    }
}
