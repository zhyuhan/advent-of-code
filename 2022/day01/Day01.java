import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day01 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day01/input.in");
        Scanner input = new Scanner(inputFile);

        PriorityQueue<Integer> cals = new PriorityQueue<>(3);
        int curr = 0;

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.equals("")) {
                if (cals.size() < 3) {
                    cals.add(curr);
                } else if (curr > cals.peek()) {
                    cals.poll();
                    cals.add(curr);
                }
                curr = 0;
            } else {
                curr += Integer.parseInt(line);
            }
        }
        input.close();

        int first = cals.poll();
        int second = cals.poll();
        int third = cals.poll();

        // part 1
        System.out.println(third);

        // part 2
        System.out.println(first + second + third);
    }
}
