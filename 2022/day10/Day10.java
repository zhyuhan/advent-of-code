import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day10 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day10/input.in");
        Scanner input = new Scanner(inputFile);

        int tick = 1, regX = 1, toAdd = 0;
        boolean isAdding = false;

        int total = 0;

        while (input.hasNextLine() || isAdding) {
            if ((tick - 20) % 40 == 0) {
                total += tick * regX;
            }

            // part 2
            int drawPos = (tick - 1) % 40;

            if (Math.abs(regX - drawPos) <= 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }

            if (tick % 40 == 0) {
                System.out.println();
            }
            // part 2 end

            if (isAdding) {
                regX += toAdd;
                isAdding = false;
            } else {
                String inst = input.nextLine();
                if (!inst.equals("noop")) {
                    isAdding = true;
                    toAdd = Integer.parseInt(inst.split(" ")[1]);
                }
            }

            tick++;
        }
        input.close();

        // part 1
        System.out.println(total);
    }
}
