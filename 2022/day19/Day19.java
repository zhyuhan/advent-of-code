import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {
    static Map<String, Integer> memo = new HashMap<>();
    static List<int[][]> blueprints = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day19/input.in");
        Scanner input = new Scanner(inputFile);

        Pattern blueprintRegex = Pattern.compile(
                ".+(\\d+) ore\\..+(\\d+) ore\\..+(\\d+) ore and (\\d+) clay\\..+(\\d+) ore and (\\d+) obsidian\\.");

        while (input.hasNextLine()) {
            Matcher m = blueprintRegex.matcher(input.nextLine());
            if (m.matches()) {
                int oreCost = Integer.parseInt(m.group(1));
                int clayCost = Integer.parseInt(m.group(2));
                int obsidianCost1 = Integer.parseInt(m.group(3));
                int obsidianCost2 = Integer.parseInt(m.group(4));
                int geodeCost1 = Integer.parseInt(m.group(5));
                int geodeCost2 = Integer.parseInt(m.group(6));
                blueprints.add(new int[][] { { oreCost, 0, 0, 0 }, { clayCost, 0, 0, 0 },
                        { obsidianCost1, obsidianCost2, 0, 0 }, { geodeCost1, 0, geodeCost2, 0 } });
            }
        }
        input.close();

        // part 1
        int qualityLevels = 0;
        for (int i = 0; i < blueprints.size(); i++) {
            qualityLevels += (i + 1) * work(i, 24, new int[] { 0, 0, 0, 0 }, new int[] { 1, 0, 0, 0 });
        }
        System.out.println(qualityLevels);

        // part 2
        int total = 1;
        for (int i = 0; i < 3; i++) {
            total *= work(i, 32, new int[] { 0, 0, 0, 0 }, new int[] { 1, 0, 0, 0 });
        }
        System.out.println(total);
    }

    static int work(int blueprintNum, int timeLeft, int[] materials, int[] robots) {
        StringBuilder memoKeyBuilder = new StringBuilder(blueprintNum + "," + timeLeft);
        for (int m : materials) {
            memoKeyBuilder.append(",").append(m);
        }
        for (int r : robots) {
            memoKeyBuilder.append(",").append(r);
        }
        String memoKey = memoKeyBuilder.toString();

        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }

        if (timeLeft == 0) {
            return materials[3];
        }

        int[][] blueprint = blueprints.get(blueprintNum);

        int maxGeodes = materials[3] + robots[3] * timeLeft;

        for (int robotType = 0; robotType < 4; robotType++) {
            if (robotType < 3) {
                // we only need as many robots of any time as the max materials needed to build
                // any non-geode robot
                int maxRobotsNeeded = 0;
                for (int i = 0; i < 4; i++) {
                    maxRobotsNeeded = Math.max(maxRobotsNeeded, blueprint[i][robotType]);
                }
                if (robots[robotType] >= maxRobotsNeeded) {
                    continue;
                }
            }

            int[] robotCosts = blueprint[robotType];

            int miningTime = 0;
            boolean canBuild = true;
            for (int i = 0; i < 4; i++) {
                if (robotCosts[i] == 0) {
                    continue;
                }
                if (robots[i] == 0) {
                    canBuild = false;
                    break;
                }
                miningTime = Math.max(miningTime, Math.ceilDiv(robotCosts[i] - materials[i], robots[i]));
            }
            miningTime++; // time to build

            if (!canBuild || timeLeft < miningTime) {
                continue;
            }

            int[] newMaterials = materials.clone();
            int[] newRobots = robots.clone();
            for (int i = 0; i < 4; i++) {
                newMaterials[i] += robots[i] * miningTime - robotCosts[i];
            }
            newRobots[robotType]++;

            maxGeodes = Math.max(maxGeodes, work(blueprintNum, timeLeft - miningTime, newMaterials, newRobots));
        }

        memo.put(memoKey, maxGeodes);
        return maxGeodes;
    }
}
