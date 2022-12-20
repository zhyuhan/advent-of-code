import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {
    static Map<String, Integer> memo = new HashMap<>();
    static Map<String, Integer> valves = new HashMap<>();
    static List<Integer> flowRates = new ArrayList<>();
    static Map<String, String[]> tunnels = new HashMap<>();
    static int[][] dist = new int[100][100];
    static String startValve = "AA";
    static int totalTime = 30, totalTimeWithElephant = 26;

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day16/input.in");
        Scanner input = new Scanner(inputFile);

        Pattern valveRegex = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)");

        int count = 0;

        while (input.hasNextLine()) {
            Matcher m = valveRegex.matcher(input.nextLine());
            if (m.matches()) {
                String name = m.group(1);
                int flowRate = Integer.parseInt(m.group(2));
                String[] adjacent = m.group(3).split(", ");

                valves.put(name, count++);
                flowRates.add(flowRate);
                tunnels.put(name, adjacent);
            }
        }
        input.close();

        for (int i = 0; i < valves.size(); i++) {
            for (int j = 0; j < valves.size(); j++) {
                dist[i][j] = 999;
            }
        }

        for (Map.Entry<String, String[]> entry : tunnels.entrySet()) {
            String valve = entry.getKey();
            for (String t : entry.getValue()) {
                dist[valves.get(valve)][valves.get(t)] = 1;
            }
        }

        // floyd-warshall
        for (int k = 0; k < valves.size(); k++) {
            for (int i = 0; i < valves.size(); i++) {
                for (int j = 0; j < valves.size(); j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        List<Integer> valvesToOpen = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : valves.entrySet()) {
            if (!entry.getKey().equals(startValve) && flowRates.get(entry.getValue()) > 0) {
                valvesToOpen.add(entry.getValue());
            }
        }

        // part 1
        System.out.println(openValves(valves.get(startValve), valvesToOpen, totalTime, false));

        // part 2
        System.out.println(openValves(valves.get(startValve), valvesToOpen, totalTimeWithElephant, true));
    }

    static int openValves(int curr, List<Integer> rest, int timeLeft, boolean withElephant) {
        StringBuilder memoKeyBuilder = new StringBuilder(curr + "," + timeLeft + "," + withElephant);
        for (Integer valve : rest) {
            memoKeyBuilder.append(",").append(valve);
        }
        String memoKey = memoKeyBuilder.toString();

        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }

        int pressure = !withElephant ? 0 : openValves(valves.get(startValve), rest, totalTimeWithElephant, false);

        for (Integer next : rest) {
            if (dist[curr][next] <= timeLeft) {
                List<Integer> newRest = new ArrayList<>(rest);
                newRest.remove(next);
                pressure = Math.max(
                        pressure,
                        flowRates.get(next) * (timeLeft - 1 - dist[curr][next]) +
                                openValves(next, newRest, timeLeft - 1 - dist[curr][next], withElephant));
            }
        }

        memo.put(memoKey, pressure);
        return pressure;
    }
}
