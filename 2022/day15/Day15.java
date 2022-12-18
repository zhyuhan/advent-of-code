import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {
    static Map<Integer, Set<Integer>> beacons = new HashMap<>();
    static Map<String, Integer> zones = new HashMap<>();
    static Map<Integer, List<int[]>> impossibleIntervals = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day15/input.in");
        Scanner input = new Scanner(inputFile);

        Pattern coordRegex = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

        while (input.hasNextLine()) {
            Matcher m = coordRegex.matcher(input.nextLine());
            if (m.matches()) {
                int sensorX = Integer.parseInt(m.group(1));
                int sensorY = Integer.parseInt(m.group(2));
                int beaconX = Integer.parseInt(m.group(3));
                int beaconY = Integer.parseInt(m.group(4));

                int manDist = manhattanDist(sensorX, sensorY, beaconX, beaconY);

                zones.put(sensorX + "," + sensorY, manDist);
                beacons.putIfAbsent(beaconY, new HashSet<>());
                beacons.get(beaconY).add(beaconX);
            }
        }
        input.close();

        // part 1
        System.out.println(numRowImpossible(2000000));

        // part 2
        System.out.println(distressFrequency(4000000));
    }

    static int manhattanDist(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    static List<int[]> rowImpossibleIntervals(int row) {
        List<int[]> rowIntervals = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : zones.entrySet()) {
            List<Integer> coord = Arrays.stream(entry.getKey().split(",")).map(Integer::parseInt).toList();
            int sensorX = coord.get(0);
            int sensorY = coord.get(1);
            int size = entry.getValue();

            int verticalDist = Math.abs(row - sensorY);

            if (verticalDist > size) {
                continue;
            }

            int zoneLeft = sensorX - size + verticalDist;
            int zoneRight = sensorX + size - verticalDist;

            rowIntervals.add(new int[]{zoneLeft, zoneRight});
        }

        rowIntervals.sort(Comparator.comparingInt(a -> a[0]));

        LinkedList<int[]> merged = new LinkedList<>();

        for (int[] interval : rowIntervals) {
            if (merged.isEmpty() || merged.getLast()[1] < interval[0]) {
                merged.add(interval);
            } else {
                merged.getLast()[1] = Math.max(merged.getLast()[1], interval[1]);
            }
        }

        impossibleIntervals.put(row, merged);
        return merged;
    }

    static int numRowImpossible(int row) {
        List<int[]> intervals = rowImpossibleIntervals(row);

        int count = 0;
        for (int[] interval : intervals) {
            count += interval[1] - interval[0] + 1;
            for (Integer beaconX : beacons.get(row)) {
                if (beaconX >= interval[0] && beaconX <= interval[1]) {
                    count--;
                }
            }
        }
        return count;
    }

    static long distressFrequency(int maxCoord) {
        for (int y = 0; y <= maxCoord; y++) {
            int x = 0;
            List<int[]> intervals = rowImpossibleIntervals(y);
            for (int[] interval : intervals) {
                if (x > maxCoord) {
                    continue;
                }
                if (x < interval[0]) {
                    return x * 4000000L + y;
                }
                x = interval[1] + 1;
            }
        }
        return -1;
    }
}
