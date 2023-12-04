pub fn solve(input: &str) -> u32 {
    let card_winnings = input
        .lines()
        .map(|line| {
            let card = line.split(": ").nth(1).unwrap();
            get_winnings(card)
        })
        .collect::<Vec<u32>>();

    let mut num_cards_won = card_winnings.iter().map(|_| 1).collect::<Vec<u32>>();

    for i in 0..num_cards_won.len() {
        if num_cards_won[i] == 0 {
            continue;
        }

        for c in 0..card_winnings[i] {
            num_cards_won[i + usize::try_from(c).unwrap() + 1] += num_cards_won[i];
        }
    }

    num_cards_won.iter().sum()
}

fn get_winnings(card_str: &str) -> u32 {
    let mut winnings = 0;

    let nums = card_str.split(" | ");
    let win_nums = nums
        .clone()
        .nth(0)
        .unwrap()
        .split(" ")
        .filter_map(|num| num.parse::<u32>().ok())
        .collect::<Vec<u32>>();
    let my_nums = nums
        .clone()
        .nth(1)
        .unwrap()
        .split(" ")
        .filter_map(|num| num.parse::<u32>().ok())
        .collect::<Vec<u32>>();

    for num in my_nums.iter() {
        if win_nums.contains(num) {
            winnings += 1;
        }
    }

    winnings
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11";
        let output = solve(input);
        assert_eq!(output, 30);
    }
}
