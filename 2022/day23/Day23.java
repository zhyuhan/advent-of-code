import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day23 {
    public static Map<Integer, Map<Integer, Elf>> elfMap = new HashMap<>();
    static int width = 0, height = 0;
    static int minX = 0, maxX = 0, minY = 0, maxY = 0;

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day23/input.in");
        Scanner input = new Scanner(inputFile);

        List<Elf> elfs = new ArrayList<>();

        while (input.hasNextLine()) {
            String line = input.nextLine();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#') {
                    elfs.add(new Elf(i, height));
                }
            }
            width = line.length();
            height++;
        }
        input.close();

        minX = 0;
        maxX = width - 1;
        minY = 0;
        maxY = height - 1;

        for (Elf elf : elfs) {
            elfMap.putIfAbsent(elf.y, new HashMap<>());
            elfMap.get(elf.y).put(elf.x, elf);
        }

        int[][] directions = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

        Map<String, List<Elf>> proposals = new HashMap<>();

        int round = 0;
        boolean noneMoved = false;

        while (!noneMoved) {
            noneMoved = true;
            proposals.clear();

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    if (elfMap.get(y) == null) continue;

                    Elf elf = elfMap.get(y).get(x);
                    if (elf == null) continue;

                    boolean isEmpty = true;
                    for (int[] dir : directions) {
                        int dx = x + dir[0], dy = y + dir[1];
                        if (dx < minX || dx > maxX || dy < minY || dy > maxY) continue;
                        if (elfMap.get(dy) == null || elfMap.get(dy).get(dx) == null) continue;
                        isEmpty = false;
                        break;
                    }

                    if (isEmpty) {
                        continue;
                    }

                    int[] proposal = elf.propose(round);
                    if (proposal == null) continue;
                    String proposalKey = proposal[0] + "," + proposal[1];
                    proposals.putIfAbsent(proposalKey, new ArrayList<>());
                    proposals.get(proposalKey).add(elf);
                }
            }

            for (Map.Entry<String, List<Elf>> entry : proposals.entrySet()) {
                if (entry.getValue().size() > 1) {
                    continue;
                }

                List<Integer> proposal = Arrays.stream(entry.getKey().split(",")).map(Integer::parseInt).toList();
                int x = proposal.get(0), y = proposal.get(1);
                if (x < minX) {
                    minX--;
                    width++;
                } else if (x > maxX) {
                    maxX++;
                    width++;
                }
                if (y < minY) {
                    minY--;
                    height++;
                } else if (y > maxY) {
                    maxY++;
                    height++;
                }
                for (Elf elf : entry.getValue()) {
                    elf.moveTo(x, y);
                    noneMoved = false;
                }
            }

            round++;

            // part 1
            if (round == 10) {
                System.out.println(countEmptyTiles());
            }
        }

        printMap();

        // part 2
        System.out.println(round);
    }

    static int countEmptyTiles() {
        int count = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (elfMap.get(y) == null || elfMap.get(y).get(x) == null) {
                    count++;
                }
            }
        }
        return count;
    }

    static void printMap() {
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (elfMap.get(y) == null || elfMap.get(y).get(x) == null) {
                    System.out.print(".");
                } else {
                    System.out.print("#");
                }
            }
            System.out.println();
        }
    }
}

class Elf {
    static int[][][] directions = {
            {{0, -1}, {1, -1}, {-1, -1}},
            {{0, 1}, {1, 1}, {-1, 1}},
            {{-1, 0}, {-1, -1}, {-1, 1}},
            {{1, 0}, {1, -1}, {1, 1}}
    };
    static int[][] moves = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    public int x, y;

    Elf(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] propose(int round) {
        int[] move = null;

        for (int i = 0; i < 4; i++) {
            int index = (round + i) % 4;
            boolean isEmpty = true;
            for (int[] dir : directions[index]) {
                int dx = dir[0], dy = dir[1];
                if (x + dx < Day23.minX || x + dx > Day23.maxX || y + dy < Day23.minY || y + dy > Day23.maxY) continue;
                if (Day23.elfMap.get(y + dy) == null || Day23.elfMap.get(y + dy).get(x + dx) == null) continue;
                isEmpty = false;
                break;
            }

            if (isEmpty) {
                move = moves[index];
                break;
            }
        }

        if (move == null) {
            return null;
        }
        return new int[]{x + move[0], y + move[1]};
    }

    public void moveTo(int x, int y) {
        Day23.elfMap.get(this.y).remove(this.x);
        Day23.elfMap.putIfAbsent(y, new HashMap<>());
        Day23.elfMap.get(y).put(x, this);
        this.x = x;
        this.y = y;
    }
}
