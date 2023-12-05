pub fn solve(input: &str) -> u64 {
    let lines = input.lines();

    let seeds = lines
        .clone()
        .nth(0)
        .unwrap()
        .split(": ")
        .nth(1)
        .unwrap()
        .split_whitespace()
        .filter_map(|num| num.parse::<u64>().ok())
        .collect::<Vec<_>>();

    let mut seed_ranges = Vec::new();

    for i in (0..seeds.len()).step_by(2) {
        let start = seeds[i];
        seed_ranges.push((start, start + seeds[i + 1] - 1, 0)); // (start, end, stage)
    }

    let mut map_lines = lines.clone().skip(2);

    let mut stage = 0;

    while let Some(line) = map_lines.next() {
        if line == "" {
            // increment stage for all seed ranges
            for i in 0..seed_ranges.len() {
                if seed_ranges[i].2 == stage {
                    seed_ranges[i].2 += 1;
                }
            }
            stage += 1;
            continue;
        }
        if line.ends_with("map:") {
            continue;
        }

        let nums = line.split_whitespace().collect::<Vec<&str>>();
        let dest = nums[0].parse::<u64>().unwrap();
        let src = nums[1].parse::<u64>().unwrap();
        let len = nums[2].parse::<u64>().unwrap();

        let map_range = (src, src + len - 1);

        for i in 0..seed_ranges.len() {
            let seed_range = seed_ranges[i];

            if seed_range.2 != stage {
                continue;
            }
            if map_range.1 < seed_range.0 || map_range.0 > seed_range.1 {
                continue;
            }

            seed_ranges.remove(i);
            let to_map = (seed_range.0.max(map_range.0), seed_range.1.min(map_range.1));

            if to_map.0 > seed_range.0 {
                seed_ranges.push((seed_range.0, to_map.0 - 1, stage));
            }
            if to_map.1 < seed_range.1 {
                seed_ranges.push((to_map.1 + 1, seed_range.1, stage));
            }
            seed_ranges.insert(i, (to_map.0 - src + dest, to_map.1 - src + dest, stage + 1));
        }
    }

    seed_ranges.iter().map(|range| range.0).min().unwrap()
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
        assert_eq!(output, 46);
    }
}
