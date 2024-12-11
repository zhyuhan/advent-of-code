import gleam/int
import gleam/io
import gleam/list
import gleam/option.{Some}
import gleam/regexp
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
  let assert Ok(re) = regexp.from_string("mul\\((\\d{1,3}),(\\d{1,3})\\)")

  input
  |> regexp.scan(re, _)
  |> list.map(fn(m) {
    let assert [Some(l), Some(r)] = m.submatches
    let a = l |> int.parse |> result.unwrap(0)
    let b = r |> int.parse |> result.unwrap(0)
    a * b
  })
  |> list.fold(0, int.add)
}

pub fn part2(input: String) -> Int {
  let assert Ok(re) =
    regexp.from_string(
      "(?:mul\\((\\d{1,3}),(\\d{1,3})\\))|(?:do\\(\\))|(?:don't\\(\\))",
    )

  input
  |> regexp.scan(re, _)
  |> list.map(fn(m) {
    case m.content {
      "do()" -> #("do()", [])
      "don't()" -> #("don't()", [])
      _ -> {
        let assert [Some(l), Some(r)] = m.submatches
        let a = l |> int.parse |> result.unwrap(0)
        let b = r |> int.parse |> result.unwrap(0)
        #("mul", [a, b])
      }
    }
  })
  |> exec(True)
}

fn exec(statements: List(#(String, List(Int))), is_enabled: Bool) -> Int {
  case statements {
    [] -> 0
    [#("do()", []), ..rest] -> exec(rest, True)
    [#("don't()", []), ..rest] -> exec(rest, False)
    [#(_, args), ..rest] -> {
      exec(rest, is_enabled)
      + case is_enabled {
        False -> 0
        True -> {
          let assert [a, b] = args
          a * b
        }
      }
    }
  }
}
