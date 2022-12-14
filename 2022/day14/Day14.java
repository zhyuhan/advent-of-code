import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day14 {
    static List<List<Character>> map;

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day14/input.in");
        Scanner input = new Scanner(inputFile);

        int minWidth = Integer.MAX_VALUE, maxWidth = 0, minHeight = 0, maxHeight = 0;
        Set<String> rocks = new HashSet<>();

        while (input.hasNextLine()) {
            String[] line = input.nextLine().split(" -> ");
            List<List<Integer>> coords = Arrays.stream(line).map(s -> Arrays.stream(s.split(",")).map(Integer::parseInt).toList()).toList();
            int startX = coords.get(0).get(0), startY = coords.get(0).get(1);
            for (List<Integer> coord : coords) {
                int x = coord.get(0), y = coord.get(1);
                minWidth = Math.min(minWidth, x);
                maxWidth = Math.max(maxWidth, x);
                maxHeight = Math.max(maxHeight, y);
                for (int dx = Math.min(startX, x); dx <= Math.max(startX, x); dx++) {
                    for (int dy = Math.min(startY, y); dy <= Math.max(startY, y); dy++) {
                        rocks.add(dx + "," + dy);
                    }
                }
                startX = x;
                startY = y;
            }
        }
        input.close();

        // add 1 extra space all around
        minWidth--;
        maxWidth++;
        maxHeight++;

        int width = maxWidth - minWidth + 1;
        int height = maxHeight - minHeight + 1;

        // part 2
        maxHeight++;
        height++;
        width = height * 2 + 1;

        maxWidth = 500 + Math.floorDiv(width, 2);
        minWidth = 500 - Math.floorDiv(width, 2);

        for (int x = minWidth; x <= maxWidth; x++) {
            rocks.add(x + "," + maxHeight);
        }
        // part 2 end

        map = new ArrayList<>(height);
        for (int row = 0; row < height; row++) {
            List<Character> r = new ArrayList<>(width);
            for (int col = 0; col < width; col++) {
                r.add('.');
            }
            map.add(r);
        }

        for (String rock : rocks) {
            List<Integer> coord = Arrays.stream(rock.split(",")).map(Integer::parseInt).toList();
            int x = coord.get(0) - minWidth, y = coord.get(1) - minHeight;
            map.get(y).set(x, '#');
        }

        int numSand = 0;
        while (dropSand(500 - minWidth, 0)) {
            numSand++;
        }

        printMap();

        System.out.println(numSand);
    }

    static boolean dropSand(int x, int y) {
        int height = map.size(), width = map.get(0).size();
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        } else if (y == height - 1) {
            return false;
        } else if (map.get(y + 1).get(x) == '.') {
            return dropSand(x, y + 1);
        } else if (x > 0 && map.get(y + 1).get(x - 1) == '.') {
            return dropSand(x - 1, y + 1);
        } else if (x < width - 1 && map.get(y + 1).get(x + 1) == '.') {
            return dropSand(x + 1, y + 1);
        } else if (map.get(y).get(x) == '.') {
            map.get(y).set(x, 'o');
            return true;
        }
        return false;
    }

    static void printMap() {
        for (List<Character> row : map) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
