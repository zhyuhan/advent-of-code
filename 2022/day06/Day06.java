import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class Day06 {
    private static int findStart(String data, int markerLength) {
        List<Character> window = new ArrayList<>(markerLength);
        int oldest = 0;

        for (int i = 0; i < markerLength; i++) {
            window.add(data.charAt(i));
        }

        for (int i = markerLength; i < data.length(); i++) {
            Set<Character> chars = new HashSet<>(window);

            if (chars.size() == markerLength) {
                return i;
            }

            window.set(oldest, data.charAt(i));
            oldest = (oldest + 1) % markerLength;
        }

        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day06/input.in");
        Scanner input = new Scanner(inputFile);
        
        String data = input.nextLine();
        input.close();

        // part 1
        System.out.println(findStart(data, 4));

        // part 2
        System.out.println(findStart(data, 14));
    }
}
