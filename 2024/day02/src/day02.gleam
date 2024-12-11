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
  input
  |> string.split("\n")
  |> list.map(fn(s) {
    s
    |> string.split(" ")
    |> list.map(fn(c) {
      let assert Ok(i) = int.parse(c)
      i
    })
  })
  |> list.map(fn(report) {
    case is_safe(report) {
      True -> 1
      False -> 0
    }
  })
  |> list.fold(0, int.add)
}

pub fn part2(input: String) -> Int {
  input
  |> string.split("\n")
  |> list.map(fn(s) {
    s
    |> string.split(" ")
    |> list.map(fn(c) {
      let assert Ok(i) = int.parse(c)
      i
    })
  })
  |> list.map(fn(report) {
    case is_safe(report) || { report |> remove_one |> list.any(is_safe) } {
      True -> 1
      False -> 0
    }
  })
  |> list.fold(0, int.add)
}

fn is_safe(report: List(Int)) -> Bool {
  let diffs = report |> list.window_by_2 |> list.map(fn(t) { t.1 - t.0 })
  diffs |> list.all(fn(d) { d > 0 && d <= 3 })
  || diffs |> list.all(fn(d) { d < 0 && d >= -3 })
}

fn remove_one(l: List(Int)) -> List(List(Int)) {
  case l {
    [] -> []
    [_] -> []
    [x, y] -> [[x], [y]]
    [x, ..xs] -> [xs, ..list.map(remove_one(xs), fn(l) { [x, ..l] })]
  }
}
