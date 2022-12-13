import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Scanner;

public class Day12 {
    static List<List<Integer>> map = new ArrayList<>();

    static int[] end = {0, 0};

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day12/input.in");
        Scanner input = new Scanner(inputFile);

        int numRows = 0;
        int[] start = {0, 0};
        List<int[]> possibleStarts = new ArrayList<>();

        while (input.hasNextLine()) {
            String line = input.nextLine();
            List<Integer> row = new ArrayList<>();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == 'S') {
                    row.add(0);
                    start[0] = numRows;
                    start[1] = i;
                    possibleStarts.add(start);
                } else if (c == 'E') {
                    row.add(25);
                    end[0] = numRows;
                    end[1] = i;
                } else {
                    row.add(line.charAt(i) - 'a');
                    if (line.charAt(i) == 'a') {
                        possibleStarts.add(new int[]{numRows, i});
                    }
                }
            }
            map.add(row);
            numRows++;
        }
        input.close();

        // part 1
        System.out.println(movesToEnd(List.of(start)));

        // part 2
        System.out.println(movesToEnd(possibleStarts));
    }

    static int movesToEnd(List<int[]> starts) {
        int numRows = map.size(), numCols = map.get(0).size();

        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        for (int[] start : starts) {
            queue.add(new int[]{start[0], start[1], 0});
            visited.add(start[0] + "," + start[1]);
        }

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int row = curr[0], col = curr[1], count = curr[2];

            int[][] nextMoves = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
            for (int[] next : nextMoves) {
                int r = next[0];
                int c = next[1];

                if (r < 0 || r >= numRows || c < 0 || c >= numCols || visited.contains(r + "," + c)) {
                    continue;
                }
                if (map.get(r).get(c) > map.get(row).get(col) + 1) {
                    continue;
                }

                if (r == end[0] && c == end[1]) {
                    return count + 1;
                }

                queue.add(new int[]{r, c, count + 1});
                visited.add(r + "," + c);
            }
        }
        return -1;
    }
}
