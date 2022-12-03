import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

public class Day03 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day03/input.in");
        Scanner input = new Scanner(inputFile);

        int total = 0, badge_total = 0;

        int line = 0;
        HashMap<Integer, Integer> group_items = new HashMap<>();

        while (input.hasNextLine()) {
            int[] items = input.nextLine().chars().map(c -> Character.isUpperCase(c) ? c - 'A' + 27 : c - 'a' + 1).toArray();
            int comp_size = items.length / 2;

            Set<Integer> comp1 = new HashSet<>();
            
            for (int i = 0; i < comp_size; i++) {
                comp1.add(items[i]);
            }

            Set<Integer> comp2 = new HashSet<>();
            boolean found = false;

            for (int i = comp_size; i < comp_size * 2; i++) {
                if (comp1.contains(items[i])) {
                    if (!found) {
                        total += items[i];
                        found = true;
                    }
                }
                comp2.add(items[i]);
            }

            for (Integer item : comp1) {
                group_items.merge(item, 1, Integer::sum);
            }
            for (Integer item : comp2) {
                group_items.merge(item, 1, Integer::sum);
            }

            if (line == 2) {
                for (HashMap.Entry<Integer, Integer> entry : group_items.entrySet()) {
                    if (entry.getValue() == 3) {
                        badge_total += entry.getKey();
                        group_items.clear();
                        break;
                    }
                }
            }
            line = (line + 1) % 3;
        }
        input.close();

        // part 1
        System.out.println(total);

        // part 2
        System.out.println(badge_total);
    }
}
