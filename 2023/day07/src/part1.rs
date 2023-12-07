use std::{
    cmp::{Ordering, Reverse},
    collections::HashMap,
};

pub fn solve(input: &str) -> u32 {
    let mut plays = input
        .lines()
        .map(|line| {
            let mut iter = line.split_whitespace();
            let hand = iter.next().unwrap();
            let bid = iter.next().unwrap().parse::<u32>().unwrap();

            (HandType::new(hand), bid)
        })
        .collect::<Vec<_>>();

    plays.sort();

    plays
        .iter()
        .enumerate()
        .map(|p| {
            let rank = u32::try_from(p.0 + 1).unwrap();
            let bid = p.1 .1;
            bid * rank
        })
        .sum()
}

#[derive(Eq, Ord, Debug)]
enum HandType {
    FiveOfAKind((u32, u32, u32, u32, u32)),
    FourOfAKind((u32, u32, u32, u32, u32)),
    FullHouse((u32, u32, u32, u32, u32)),
    ThreeOfAKind((u32, u32, u32, u32, u32)),
    TwoPair((u32, u32, u32, u32, u32)),
    OnePair((u32, u32, u32, u32, u32)),
    HighCard((u32, u32, u32, u32, u32)),
}

impl HandType {
    pub fn new(s: &str) -> HandType {
        let cards = s.chars().map(card_value).collect::<Vec<_>>();

        let mut card_count = HashMap::new();
        for c in &cards {
            let count = card_count.entry(c).or_insert(0);
            *count += 1;
        }

        let mut unique_cards = card_count.iter().collect::<Vec<_>>();
        unique_cards.sort_by_key(|a| Reverse(a.1));

        let cards = (cards[0], cards[1], cards[2], cards[3], cards[4]);

        if *unique_cards[0].1 == 5 {
            HandType::FiveOfAKind(cards)
        } else if *unique_cards[0].1 == 4 {
            HandType::FourOfAKind(cards)
        } else if *unique_cards[0].1 == 3 && *unique_cards[1].1 == 2 {
            HandType::FullHouse(cards)
        } else if *unique_cards[0].1 == 3 {
            HandType::ThreeOfAKind(cards)
        } else if *unique_cards[0].1 == 2 && *unique_cards[1].1 == 2 {
            HandType::TwoPair(cards)
        } else if *unique_cards[0].1 == 2 {
            HandType::OnePair(cards)
        } else {
            HandType::HighCard(cards)
        }
    }
}

impl PartialEq for HandType {
    fn eq(&self, other: &Self) -> bool {
        match self {
            HandType::FiveOfAKind(a) => match other {
                HandType::FiveOfAKind(b) => a == b,
                _ => false,
            },
            HandType::FourOfAKind(a) => match other {
                HandType::FourOfAKind(b) => a == b,
                _ => false,
            },
            HandType::FullHouse(a) => match other {
                HandType::FullHouse(b) => a == b,
                _ => false,
            },
            HandType::ThreeOfAKind(a) => match other {
                HandType::ThreeOfAKind(b) => a == b,
                _ => false,
            },
            HandType::TwoPair(a) => match other {
                HandType::TwoPair(b) => a == b,
                _ => false,
            },
            HandType::OnePair(a) => match other {
                HandType::OnePair(b) => a == b,
                _ => false,
            },
            HandType::HighCard(a) => match other {
                HandType::HighCard(b) => a == b,
                _ => false,
            },
        }
    }
}

impl PartialOrd for HandType {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        match self {
            HandType::FiveOfAKind(a) => match other {
                HandType::FiveOfAKind(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Greater),
            },
            HandType::FourOfAKind(a) => match other {
                HandType::FiveOfAKind(_) => Some(Ordering::Less),
                HandType::FourOfAKind(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Greater),
            },
            HandType::FullHouse(a) => match other {
                HandType::FiveOfAKind(_) => Some(Ordering::Less),
                HandType::FourOfAKind(_) => Some(Ordering::Less),
                HandType::FullHouse(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Greater),
            },
            HandType::ThreeOfAKind(a) => match other {
                HandType::FiveOfAKind(_) => Some(Ordering::Less),
                HandType::FourOfAKind(_) => Some(Ordering::Less),
                HandType::FullHouse(_) => Some(Ordering::Less),
                HandType::ThreeOfAKind(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Greater),
            },
            HandType::TwoPair(a) => match other {
                HandType::FiveOfAKind(_) => Some(Ordering::Less),
                HandType::FourOfAKind(_) => Some(Ordering::Less),
                HandType::FullHouse(_) => Some(Ordering::Less),
                HandType::ThreeOfAKind(_) => Some(Ordering::Less),
                HandType::TwoPair(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Greater),
            },
            HandType::OnePair(a) => match other {
                HandType::FiveOfAKind(_) => Some(Ordering::Less),
                HandType::FourOfAKind(_) => Some(Ordering::Less),
                HandType::FullHouse(_) => Some(Ordering::Less),
                HandType::ThreeOfAKind(_) => Some(Ordering::Less),
                HandType::TwoPair(_) => Some(Ordering::Less),
                HandType::OnePair(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Greater),
            },
            HandType::HighCard(a) => match other {
                HandType::HighCard(b) => Some(a.cmp(b)),
                _ => Some(Ordering::Less),
            },
        }
    }
}

fn card_value(c: char) -> u32 {
    match c {
        'A' => 14,
        'K' => 13,
        'Q' => 12,
        'J' => 11,
        'T' => 10,
        _ => c.to_digit(10).unwrap(),
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_solve() {
        let input = "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483";
        let output = solve(input);
        assert_eq!(output, 6440);
    }
}
