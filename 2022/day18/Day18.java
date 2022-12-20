import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day18 {
    static int[][][] map = new int[100][100][100];
    static int minX = 100, maxX = 0, minY = 100, maxY = 0, minZ = 100, maxZ = 0;
    static int width, height, depth;

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day18/input.in");
        Scanner input = new Scanner(inputFile);

        List<int[]> cubes = new ArrayList<>();

        while (input.hasNextLine()) {
            List<Integer> pos = Arrays.stream(input.nextLine().split(",")).map(Integer::parseInt).toList();
            int x = pos.get(0), y = pos.get(1), z = pos.get(2);
            cubes.add(new int[]{x, y, z});

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            minZ = Math.min(minZ, z);
            maxZ = Math.max(maxZ, z);
        }
        input.close();

        minX--;
        maxX++;
        minY--;
        maxY++;
        minZ--;
        maxZ++;
        width = maxX - minX + 1;
        height = maxY - minY + 1;
        depth = maxZ - minZ + 1;

        for (int[] pos : cubes) {
            int x = pos[0], y = pos[1], z = pos[2];
            map[x - minX][y - minY][z - minZ] = 1;
        }

        // part 1
        int exposedFaces = 0;

        for (int[] pos : cubes) {
            exposedFaces += 6;
            int x = pos[0] - minX, y = pos[1] - minY, z = pos[2] - minZ;


            int[] d = {-1, 1};

            for (int dx : d) {
                exposedFaces -= map[x + dx][y][z];
            }
            for (int dy : d) {
                exposedFaces -= map[x][y + dy][z];
            }
            for (int dz : d) {
                exposedFaces -= map[x][y][z + dz];
            }
        }

        System.out.println(exposedFaces);

        // part 2
        floodFill(0, 0, 0);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    if (map[x][y][z] == 0) {
                        int[] d = {-1, 1};

                        for (int dx : d) {
                            exposedFaces -= map[x + dx][y][z];
                        }
                        for (int dy : d) {
                            exposedFaces -= map[x][y + dy][z];
                        }
                        for (int dz : d) {
                            exposedFaces -= map[x][y][z + dz];
                        }
                    }
                }
            }
        }

        System.out.println(exposedFaces);
    }

    static void floodFill(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) {
            return;
        }
        if (map[x][y][z] != 0) {
            return;
        }

        map[x][y][z] = 2;

        int[] d = {-1, 1};

        for (int dx : d) {
            floodFill(x + dx, y, z);
        }
        for (int dy : d) {
            floodFill(x, y + dy, z);
        }
        for (int dz : d) {
            floodFill(x, y, z + dz);
        }
    }
}
