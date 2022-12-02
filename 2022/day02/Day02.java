import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day02 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day02/input.in");
        Scanner input = new Scanner(inputFile);

        int part1_score = 0, part2_score = 0;

        while (input.hasNextLine()) {
            char p1 = input.next().charAt(0);
            char p2 = input.next().charAt(0);

            // part 1
            part1_score += p2 - 'X' + 1;

            switch (p1) {
                case 'A' -> part1_score += p2 == 'Y' ? 6 : p2 == 'X' ? 3 : 0;
                case 'B' -> part1_score += p2 == 'Z' ? 6 : p2 == 'Y' ? 3 : 0;
                default -> part1_score += p2 == 'X' ? 6 : p2 == 'Z' ? 3 : 0;
            }

            // part 2
            int to_play = 0;
            switch (p2) {
                case 'X' -> {
                    part2_score += 0;
                    to_play = (p1 - 'A' + 2) % 3;
                }
                case 'Y' -> {
                    part2_score += 3;
                    to_play = p1 - 'A';
                }
                case 'Z' -> {
                    part2_score += 6;
                    to_play = (p1 - 'A' + 1) % 3;
                }
            }

            part2_score += to_play + 1;
        }
        input.close();

        System.out.println(part1_score);
        System.out.println(part2_score);
    }
}
