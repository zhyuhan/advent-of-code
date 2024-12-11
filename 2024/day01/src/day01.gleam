import gleam/dict
import gleam/int
import gleam/io
import gleam/list
import gleam/option.{None, Some}
import gleam/string
import simplifile.{read}

pub fn main() {
  let assert Ok(input) = read("input.in")

  part1(input)
  |> io.debug

  part2(input)
  |> io.debug
}

pub fn part1(input: String) -> Int {
  let assert [left, right] =
    input
    |> string.split("\n")
    |> list.filter(fn(s) { s != "" })
    |> list.map(fn(s) {
      string.split(s, "   ")
      |> list.filter_map(int.parse)
    })
    |> list.transpose
    |> list.map(fn(l) { list.sort(l, int.compare) })

  list.map2(left, right, fn(l, r) { int.absolute_value(l - r) })
  |> list.fold(0, int.add)
}

pub fn part2(input: String) -> Int {
  let assert [left, right] =
    input
    |> string.split("\n")
    |> list.filter(fn(s) { s != "" })
    |> list.map(fn(s) {
      string.split(s, "   ")
      |> list.filter_map(int.parse)
    })
    |> list.transpose
    |> list.map(fn(l) { list.sort(l, int.compare) })

  let freq =
    right
    |> list.fold(dict.new(), fn(d, k) {
      dict.upsert(d, k, fn(x) {
        case x {
          Some(i) -> i + 1
          None -> 1
        }
      })
    })

  left
  |> list.map(fn(l) {
    l
    * case dict.get(freq, l) {
      Ok(i) -> i
      Error(Nil) -> 0
    }
  })
  |> list.fold(0, int.add)
}
