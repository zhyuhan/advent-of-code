import day04.{part1, part2}
import gleeunit
import gleeunit/should

pub fn main() {
  gleeunit.main()
}

// gleeunit test functions end in `_test`
pub fn part1_small_test() {
  let input =
    "..X...
.SAMX.
.A..A.
XMAS.S
.X...."

  part1(input)
  |> should.equal(4)
}

pub fn part1_test() {
  let input =
    "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"

  part1(input)
  |> should.equal(18)
}

pub fn part2_test() {
  let input =
    "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"

  part2(input)
  |> should.equal(9)
}
