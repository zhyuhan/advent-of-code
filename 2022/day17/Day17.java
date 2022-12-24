import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day17 {
    static int[][][] rocks = {
            {{1, 1, 1, 1}},
            {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}},
            {{1, 1, 1}, {0, 0, 1}, {0, 0, 1}},
            {{1}, {1}, {1}, {1}},
            {{1, 1}, {1, 1}}
    };
    static List<int[]> cave = new ArrayList<>();
    static String jet;
    static int width = 7, left = 2, top = 3;
    static long height = top;
    static int rockIndex = 0, jetIndex = 0;
    static Map<String, int[]> cache = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day17/input.in");
        Scanner input = new Scanner(inputFile);

        jet = input.nextLine();
        input.close();

        extendCave(top);

        long numRocks = 1, rocksToDrop = 1000000000000L;

        int rocksDiff = -1, heightDiff = -1;

        while (numRocks < rocksToDrop) {
            int newHeight = dropRock();

            String cacheKey = getCacheKey();
            if (cache.containsKey(cacheKey)) {
                int[] value = cache.get(cacheKey);
                rocksDiff = (int) numRocks - value[0];
                heightDiff = newHeight - value[1];
                break;
            }

            cache.put(cacheKey, new int[]{(int) numRocks, newHeight});
            numRocks++;
        }

        long numCycles = Math.floorDiv(rocksToDrop - numRocks, rocksDiff);
        numRocks += rocksDiff * numCycles;

        while (numRocks < rocksToDrop) {
            dropRock();
            numRocks++;
        }
        System.out.println(rockTowerHeight() + heightDiff * numCycles);
    }

    static String getCacheKey() {
        StringBuilder cacheKeyBuilder = new StringBuilder(rockIndex + "," + jetIndex);
        for (int x = 0; x < width; x++) {
            int y = 0;
            while (y <= height && cave.get((int) height - y)[x] == 0) y++;
            cacheKeyBuilder.append(",").append(y);
        }
        return cacheKeyBuilder.toString();
    }

    static int dropRock() {
        boolean isFalling = false;
        int rockX = left, rockY = (int) height;

        while (true) {
            if (isFalling) {
                if (canFall(rockIndex, rockX, rockY)) {
                    rockY--;
                } else {
                    placeRock(rockIndex, rockX, rockY);

                    int newHeight = Math.max((int) height, rockY + rocks[rockIndex].length + top);
                    int rowsToAdd = newHeight - (int) height;
                    height += rowsToAdd;
                    extendCave(rowsToAdd);
                    rockIndex = (rockIndex + 1) % rocks.length;

                    return rockTowerHeight();
                }
                isFalling = false;
            } else {
                if (jet.charAt(jetIndex) == '>') {
                    if (canMoveRight(rockIndex, rockX, rockY)) rockX++;
                } else {
                    if (canMoveLeft(rockIndex, rockX, rockY)) rockX--;
                }
                jetIndex = (jetIndex + 1) % jet.length();
                isFalling = true;
            }
        }
    }

    static boolean canMoveLeft(int rockIndex, int rockX, int rockY) {
        int[][] rock = rocks[rockIndex];

        if (rockX == 0) return false;

        int rockHeight = rock.length;
        for (int y = 0; y < rockHeight; y++) {
            if (rockY + y > height) continue;
            int x = 0;
            while (rock[y][x] == 0) x++;
            if (cave.get(rockY + y)[rockX + x - 1] != 0) {
                return false;
            }
        }
        return true;
    }

    static boolean canMoveRight(int rockIndex, int rockX, int rockY) {
        int[][] rock = rocks[rockIndex];

        int rockWidth = rock[0].length;
        if (rockX + rockWidth == width) return false;

        int rockHeight = rock.length;
        for (int y = 0; y < rockHeight; y++) {
            if (rockY + y > height) continue;
            int x = rockWidth - 1;
            while (rock[y][x] == 0) x--;
            if (cave.get(rockY + y)[rockX + x + 1] != 0) {
                return false;
            }
        }
        return true;
    }

    static boolean canFall(int rockIndex, int rockX, int rockY) {
        int[][] rock = rocks[rockIndex];

        if (rockY == 0) return false;

        int rockWidth = rock[0].length;
        for (int x = 0; x < rockWidth; x++) {
            int y = 0;
            while (rock[y][x] == 0) y++;
            if (cave.get(rockY + y - 1)[rockX + x] != 0) {
                return false;
            }
        }
        return true;
    }

    static void placeRock(int rockIndex, int rockX, int rockY) {
        int[][] rock = rocks[rockIndex];

        int rockHeight = rock.length, rockWidth = rock[0].length;
        for (int y = 0; y < rockHeight; y++) {
            for (int x = 0; x < rockWidth; x++) {
                if (rock[y][x] == 1) {
                    cave.get(rockY + y)[rockX + x] = 1;

                }
            }
        }
    }

    static int rockTowerHeight() {
        int emptyRows = 0;
        for (int y = (int) height - 1; y > -1; y--) {
            boolean isEmpty = true;
            for (int x = 0; x < width; x++) {
                if (cave.get(y)[x] != 0) {
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) break;
            emptyRows++;
        }
        return (int) height - emptyRows;
    }

    static void extendCave(int rows) {
        for (int i = 0; i <= rows; i++) {
            int[] row = new int[width];
            for (int j = 0; j < width; j++) row[i] = 0;
            cave.add(row);
        }
    }
}
