import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day20 {
    static List<Integer> numbers = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day20/input.in");
        Scanner input = new Scanner(inputFile);

        while (input.hasNextInt()) {
            int num = input.nextInt();
            numbers.add(num);
        }
        input.close();

        // part 1
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) indices.add(i);
        mixList(indices);
        System.out.println(getGroveCoordinate(indices));

        // part 2
        List<Long> decryptedNumbers = new ArrayList<>();
        for (int n : numbers) {
            decryptedNumbers.add((long) n * 811589153);
        }

        List<Integer> indices2 = new ArrayList<>();
        for (int i = 0; i < decryptedNumbers.size(); i++) indices2.add(i);

        for (int i = 0; i < 10; i++) {
            mixList(decryptedNumbers, indices2);
        }
        System.out.println(getGroveCoordinate(decryptedNumbers, indices2));
    }

    static void mixList(List<Integer> indices) {
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.indexOf(i);
            indices.remove(index);
            int newIndex = (index + numbers.get(i)) % indices.size();
            if (newIndex == 0) {
                newIndex = indices.size();
            } else if (newIndex < 0) {
                newIndex = indices.size() + newIndex;
            }
            indices.add(newIndex, i);
        }
    }

    static void mixList(List<Long> numbers, List<Integer> indices) {
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.indexOf(i);
            indices.remove(index);
            int newIndex = (int) ((index + numbers.get(i)) % indices.size());
            if (newIndex == 0) {
                newIndex = indices.size();
            } else if (newIndex < 0) {
                newIndex = indices.size() + newIndex;
            }
            indices.add(newIndex, i);
        }
    }

    static int getGroveCoordinate(List<Integer> indices) {
        int indexOfZero = indices.indexOf(numbers.indexOf(0));
        int groveCoord = 0;
        for (int offset : new int[]{1000, 2000, 3000}) {
            groveCoord += numbers.get(indices.get((indexOfZero + offset) % numbers.size()));
        }
        return groveCoord;
    }

    static long getGroveCoordinate(List<Long> numbers, List<Integer> indices) {
        int indexOfZero = indices.indexOf(numbers.indexOf((long) 0));
        long groveCoord = 0;
        for (int offset : new int[]{1000, 2000, 3000}) {
            groveCoord += numbers.get(indices.get((indexOfZero + offset) % numbers.size()));
        }
        return groveCoord;
    }
}
