import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day22 {
    static int[][] map;
    static int width = 0, height = 0, cubeSize = Integer.MAX_VALUE;
    static int[][] faces = {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}};

    // hardcoded sides
    static int[][] adjacentFaces = {{1, 2, 3, 5}, {4, 2, 0, 5}, {1, 4, 3, 0}, {4, 5, 0, 2}, {1, 5, 3, 2}, {4, 1, 0, 3}};
    static int[][] faceTurnDiff = {{0, 0, 2, 1}, {2, 1, 0, 0}, {3, 0, 3, 0}, {0, 0, 2, 1}, {2, 1, 0, 0}, {3, 0, 3, 0}};

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day22/input.in");
        Scanner input = new Scanner(inputFile);

        List<int[]> coords = new ArrayList<>();

        int[] start = {0, 0, 0}; // x, y, direction (R: 0, D: 1, L: 2, U: 3)

        List<String> moves = new ArrayList<>();
        boolean isMapEnd = false, isSetStart = false;

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.equals("")) {
                isMapEnd = true;
                continue;
            }

            if (!isMapEnd) {
                int i;
                for (i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (!isSetStart && c == '.') {
                        start[0] = i;
                        start[1] = height;
                        isSetStart = true;
                    }
                    coords.add(new int[]{i, height, c == ' ' ? 0 : c == '.' ? 1 : -1});
                }
                width = Math.max(width, i);
                height++;
                cubeSize = Math.min(cubeSize, line.strip().length());
            } else {
                int i = 0;
                while (i < line.length()) {
                    if (line.charAt(i) == 'L' || line.charAt(i) == 'R') {
                        moves.add(line.substring(0, i));
                        moves.add(line.substring(i, i + 1));
                        line = line.substring(i + 1);
                        i = 0;
                    } else {
                        i++;
                    }
                }
                moves.add(line);
                if (input.hasNextLine()) input.nextLine();
            }
        }
        input.close();

        map = new int[height][width];
        for (int[] coord : coords) {
            int x = coord[0], y = coord[1];
            map[y][x] = coord[2];
        }

        int[] currPos;

        // part 1
        currPos = start.clone();

        for (String move : moves) {
            if (move.equals("L")) {
                turnLeft(currPos);
            } else if (move.equals("R")) {
                turnRight(currPos);
            } else {
                int steps = Integer.parseInt(move);
                while (steps > 0) {
                    int[] next = nextPos(currPos);
                    if (map[next[1]][next[0]] == 1) {
                        currPos = next;
                    }
                    steps--;
                }
            }
        }

        System.out.println(password(currPos));

        // part 2
        int faceIndex = 0;
        for (int y = 0; y < height; y += cubeSize) {
            for (int x = 0; x < width; x += cubeSize) {
                if (map[y][x] != 0) {
                    faces[faceIndex][0] = x;
                    faces[faceIndex][1] = y;
                    faceIndex++;
                }
            }
        }

        currPos = start.clone();

        for (String move : moves) {
            if (move.equals("L")) {
                turnLeft(currPos);
            } else if (move.equals("R")) {
                turnRight(currPos);
            } else {
                int steps = Integer.parseInt(move);
                while (steps > 0) {
                    int[] next = nextCubePos(currPos);
                    if (map[next[1]][next[0]] == 1) {
                        currPos = next;
                    }
                    steps--;
                }
            }
        }

        System.out.println(password(currPos));
    }

    static void turnLeft(int[] pos) {
        pos[2] = pos[2] == 0 ? 3 : pos[2] - 1;
    }

    static void turnRight(int[] pos) {
        pos[2] = (pos[2] + 1) % 4;
    }

    static int[] nextPos(int[] currPos) {
        int[] next = currPos.clone();
        switch (currPos[2]) {
            case 0 -> next[0]++;
            case 1 -> next[1]++;
            case 2 -> next[0]--;
            case 3 -> next[1]--;
        }

        next[0] = next[0] < 0 ? width + next[0] : next[0] % width;
        next[1] = next[1] < 0 ? height + next[1] : next[1] % height;

        while (map[next[1]][next[0]] == 0) {
            next = nextPos(next);
        }
        return next;
    }

    static int[] nextCubePos(int[] currPos) {
        int currCubeFace = getCubeFace(currPos[0], currPos[1]);
        int currFaceStartX = faces[currCubeFace][0], currFaceStartY = faces[currCubeFace][1];

        int[] next = currPos.clone();
        boolean isOutOfBounds = false;

        switch (currPos[2]) {
            case 0 -> {
                isOutOfBounds = next[0] + 1 >= currFaceStartX + cubeSize;
                if (!isOutOfBounds) next[0]++;
            }
            case 1 -> {
                isOutOfBounds =  next[1] + 1 >= currFaceStartY + cubeSize;
                if (!isOutOfBounds) next[1]++;
            }
            case 2 -> {
                isOutOfBounds = next[0] - 1 < currFaceStartX;
                if (!isOutOfBounds) next[0]--;
            }
            case 3 -> {
                isOutOfBounds = next[1] - 1 < currFaceStartY;
                if (!isOutOfBounds) next[1]--;
            }
        }

        if (isOutOfBounds) {
            next = mapToCubeFace(currPos);
        }
        return next;
    }

    static int getCubeFace(int x, int y) {
        for (int i = 0; i < faces.length; i++) {
            int startX = faces[i][0], startY = faces[i][1];
            if (x >= startX && x < startX + cubeSize && y >= startY && y < startY + cubeSize) {
                return i;
            }
        }
        return -1;
    }

    static int[] mapToCubeFace(int[] pos) {
        int x = pos[0], y = pos[1], dir = pos[2];
        int fromFace = getCubeFace(x, y);
        int toFace = adjacentFaces[fromFace][dir];
        int fromStartX = faces[fromFace][0], fromStartY = faces[fromFace][1];
        int toStartX = faces[toFace][0], toStartY = faces[toFace][1];

        int[] newPos = {0, 0, dir};
        int turnDiff = faceTurnDiff[fromFace][dir];
        for (int i = 0; i < turnDiff; i++) turnRight(newPos);

        int dx = pos[0] - fromStartX, dy = pos[1] - fromStartY;

        switch (turnDiff) {
            case 0 -> {
                if (dir % 2 == 0) {
                    // L/R, dx = -dx
                    newPos[0] = toStartX + cubeSize - 1 - dx;
                    newPos[1] = toStartY + dy;
                } else {
                    // U/D, dy = -dy
                    newPos[0] = toStartX + dx;
                    newPos[1] = toStartY + cubeSize - 1 - dy;
                }
            }
            case 1 -> {
                if (dir == 0) {
                    // R, dx = -dx, dy = -dy
                    newPos[0] = toStartX + cubeSize - 1 - dy;
                    newPos[1] = toStartY + cubeSize - 1 - dx;
                } else if (dir == 1 || dir == 3) {
                    // D, dx = dy, dy = dx
                    newPos[0] = toStartX + dy;
                    newPos[1] = toStartY + dx;
                } else if (dir == 2) {
                    // L, dx = -dy, dy = dx
                    newPos[0] = toStartX + cubeSize - 1 - dy;
                    newPos[1] = toStartY + dx;
                }
            }
            case 2 -> {
                if (dir % 2 == 0) {
                    // L/R, dy = -dy
                    newPos[0] = toStartX + dx;
                    newPos[1] = toStartY + cubeSize - 1 - dy;
                } else {
                    // U/D
                    newPos[0] = toStartX + dy;
                    newPos[1] = toStartY + dx;
                }
            }
            case 3 -> {
                if (dir % 2 == 0) {
                    // L/R, dx = dy, dy = dx
                    newPos[0] = toStartX + dy;
                    newPos[1] = toStartY + dx;
                } else {
                    // U/D, dx = -dy, dy = -dx
                    newPos[0] = toStartX + cubeSize - 1 - dy;
                    newPos[1] = toStartY + cubeSize - 1 - dx;
                }
            }
        }
        return newPos;
    }

    static int password(int[] pos) {
        return 1000 * (pos[1] + 1) + 4 * (pos[0] + 1) + pos[2];
    }
}
