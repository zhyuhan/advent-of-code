import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day08 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day08/input.in");
        Scanner input = new Scanner(inputFile);

        List<List<Integer>> trees = new ArrayList<>();

        while (input.hasNextLine()) {
            String[] line = input.nextLine().split("");
            List<Integer> row = new ArrayList<>();
            for (String s : line) {
                row.add(Integer.parseInt(s));
            }
            trees.add(row);
        }
        input.close();

        int num_rows = trees.size();
        int num_cols = trees.get(0).size();
        int total = (num_rows - 1 + num_cols - 1) * 2;

        int max_view = Integer.MIN_VALUE;

        for (int row = 1; row < num_rows - 1; row++) {
            for (int col = 1; col < num_cols - 1; col++) {
                Integer height = trees.get(row).get(col);

                boolean visible_top = true;
                int view_top = 0;
                for (int r = row - 1; r > -1; r--) {
                    view_top++;
                    if (trees.get(r).get(col) >= height) {
                        visible_top = false;
                        break;
                    }
                }

                boolean visible_left = true;
                int view_left = 0;
                for (int c = col - 1; c > -1; c--) {
                    view_left++;
                    if (trees.get(row).get(c) >= height) {
                        visible_left = false;
                        break;
                    }
                }

                boolean visible_right = true;
                int view_right = 0;
                for (int c = col + 1; c < num_cols; c++) {
                    view_right++;
                    if (trees.get(row).get(c) >= height) {
                        visible_right = false;
                        break;
                    }
                }

                boolean visible_bottom = true;
                int view_bottom = 0;
                for (int r = row + 1; r < num_rows; r++) {
                    view_bottom++;
                    if (trees.get(r).get(col) >= height) {
                        visible_bottom = false;
                        break;
                    }
                }

                if (visible_top || visible_left || visible_right || visible_bottom) {
                    total++;
                }

                max_view = Math.max(max_view, view_top * view_left * view_right * view_bottom);
            }
        }

        // part 1
        System.out.println(total);

        // part 2
        System.out.println(max_view);
    }
}
