import gleam/int
import gleam/io
import gleam/list
import gleam/result
import gleam/string
import simplifile.{read}

pub fn main() {
  let input = read("input.in") |> result.unwrap("") |> string.trim

  part1(input)
  |> io.debug

  part2(input)
  |> io.debug
}

pub fn part1(input: String) -> Int {
  let board =
    input
    |> string.split("\n")
    |> list.map(fn(s) { string.split(s, "") })

  let horizontal =
    board
    |> list.map(fn(line) {
      line
      |> list.window(4)
    })
    |> list.flatten()
    |> count_xmas

  let vertical =
    board
    |> list.transpose
    |> list.map(fn(line) {
      line
      |> list.window(4)
    })
    |> list.flatten()
    |> count_xmas

  let diagonals =
    board
    |> get_diagonal_window(4)
    |> list.flatten()
    |> count_xmas

  horizontal + vertical + diagonals
}

pub fn part2(input: String) -> Int {
  let board =
    input
    |> string.split("\n")
    |> list.map(fn(s) { string.split(s, "") })

  board |> get_diagonal_window(3) |> count_x_mas
}

fn is_xmas(word: List(String)) -> Bool {
  let assert [a, b, c, d] = word

  { a == "X" && b == "M" && c == "A" && d == "S" }
  || { a == "S" && b == "A" && c == "M" && d == "X" }
}

fn is_mas(word: List(String)) -> Bool {
  let assert [a, b, c] = word

  { a == "M" && b == "A" && c == "S" } || { a == "S" && b == "A" && c == "M" }
}

fn count_xmas(words: List(List(String))) -> Int {
  words
  |> list.map(fn(word) {
    case is_xmas(word) {
      True -> 1
      False -> 0
    }
  })
  |> list.fold(0, int.add)
}

fn count_x_mas(pairs: List(List(List(String)))) -> Int {
  pairs
  |> list.map(fn(pair) {
    let assert [a, b] = pair
    case is_mas(a) && is_mas(b) {
      True -> 1
      False -> 0
    }
  })
  |> list.fold(0, int.add)
}

fn get_diagonal_window(
  board: List(List(String)),
  size: Int,
) -> List(List(List(String))) {
  let height = board |> list.length
  let width = board |> list.first |> result.unwrap([]) |> list.length

  list.range(1, height - size + 1)
  |> list.map(fn(y) {
    let window_rows =
      board
      |> list.drop(y - 1)
      |> list.take(size)

    let tl_to_br =
      window_rows
      |> list.index_map(fn(row, x) {
        row
        |> list.drop(x)
        |> list.take(width - { size - 1 })
      })
      |> list.transpose

    let tr_to_bl =
      window_rows
      |> list.index_map(fn(row, x) {
        row
        |> list.drop(size - x - 1)
        |> list.take(width - { size - 1 })
      })
      |> list.transpose

    list.zip(tl_to_br, tr_to_bl)
    |> list.map(fn(d) { [d.0, d.1] })
  })
  |> list.flatten
}
