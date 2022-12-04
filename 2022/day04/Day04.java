import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day04 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day04/input.in");
        Scanner input = new Scanner(inputFile);

        int total_contains = 0, total_overlaps = 0;

        while (input.hasNextLine()) {
            String[] pair = input.nextLine().split(",");
            String[] first = pair[0].split("-");
            String[] second = pair[1].split("-");

            int first_start = Integer.parseInt(first[0]);
            int first_end = Integer.parseInt(first[1]);
            int second_start = Integer.parseInt(second[0]);
            int second_end = Integer.parseInt(second[1]);

            if ((first_start >= second_start && first_end <= second_end) || (second_start >= first_start && second_end <= first_end)) {
                total_contains += 1;
            }

            if ((first_start >= second_start && first_start <= second_end) || (second_start >= first_start && second_start <= first_end)) {
                total_overlaps += 1;
            }
        }
        input.close();

        // part 1
        System.out.println(total_contains);

        // part 2
        System.out.println(total_overlaps);
    }
}
