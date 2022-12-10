import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Day09 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day09/input.in");
        Scanner input = new Scanner(inputFile);

        int[][] tails = new int[10][2];
        for (int i = 0; i < tails.length; i++) {
            tails[i] = new int[]{0, 0};
        }

        Set<String> visited = new HashSet<>();

        while (input.hasNextLine()) {
            String[] move = input.nextLine().split(" ");
            String direction = move[0];
            int steps = Integer.parseInt(move[1]);

            for (int i = 0; i < steps; i++) {
                switch (direction) {
                    case "U" -> tails[0][1]++;

                    case "D" -> tails[0][1]--;

                    case "L" -> tails[0][0]--;

                    case "R" -> tails[0][0]++;

                }

                updateTails(tails);

                int[] last = tails[tails.length - 1];
                visited.add(last[0] + "," + last[1]);
            }

        }
        input.close();

        System.out.println(visited.size());
    }

    static void updateTails(int[][] tails) {
        for (int i = 1; i < tails.length; i++) {
            int[] prev = tails[i - 1];
            int[] curr = tails[i];
            if (prev[0] == curr[0]) {
                if (prev[1] > curr[1] + 1) {
                    curr[1]++;
                } else if (prev[1] < curr[1] - 1) {
                    curr[1]--;
                }
            } else if (prev[1] == curr[1]) {
                if (prev[0] > curr[0] + 1) {
                    curr[0]++;
                } else if (prev[0] < curr[0] - 1) {
                    curr[0]--;
                }
            } else {
                if (prev[0] > curr[0] + 1) {
                    curr[0]++;
                    curr[1] += prev[1] > curr[1] ? 1 : -1;
                } else if (prev[0] < curr[0] - 1) {
                    curr[0]--;
                    curr[1] += prev[1] > curr[1] ? 1 : -1;
                } else if (prev[1] > curr[1] + 1) {
                    curr[1]++;
                    curr[0] += prev[0] > curr[0] ? 1 : -1;
                } else if (prev[1] < curr[1] - 1) {
                    curr[1]--;
                    curr[0] += prev[0] > curr[0] ? 1 : -1;
                }
            }
        }
    }
}
