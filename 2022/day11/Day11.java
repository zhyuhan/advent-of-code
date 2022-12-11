import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day11 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day11/input.in");
        Scanner input = new Scanner(inputFile);

        List<Monkey> monkeys = new ArrayList<>();

        while (input.hasNextLine()) {
            String line = input.nextLine();

            if (line.equals("")) {
                continue;
            }

            if (line.startsWith("Monkey")) {
                Monkey monkey = new Monkey();

                String[] items = input.nextLine().substring(18).split(", ");
                for (String item : items) {
                    monkey.addItem(Long.parseLong(item));
                }

                String[] operation = input.nextLine().substring(23).split(" ");
                monkey.setOperation(operation);

                int testOp = Integer.parseInt(input.nextLine().substring(21));
                int trueTarget = Integer.parseInt(input.nextLine().substring(29));
                int falseTarget = Integer.parseInt(input.nextLine().substring(30));
                monkey.setTest(new int[]{testOp, trueTarget, falseTarget});

                monkeys.add(monkey);
            }
        }
        input.close();

        // part 1
        // System.out.println(monkeyBusiness(monkeys, true, 20));

        // part 2
        System.out.println(monkeyBusiness(monkeys, false, 10000));
    }

    static long monkeyBusiness(List<Monkey> monkeys, boolean reliefAfterInspect, int rounds) {
        // test operands are all primes, so we can prevent the worry levels from growing too large
        // using mod of lcm of the test operands
        int lcm = 1;
        for (Monkey monkey : monkeys) {
            lcm *= monkey.testOp();
        }

        while (rounds > 0) {
            for (Monkey monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    long item = monkey.inspect(reliefAfterInspect);
                    int target = monkey.test(item);
                    monkeys.get(target).addItem(item % lcm);
                }
            }
            rounds--;
        }

        List<Integer> inspections = monkeys.stream().map(monkey -> monkey.inspections).sorted((a, b) -> b - a).toList();
        return (long) inspections.get(0) * inspections.get(1);
    }
}

class Monkey {
    public final Queue<Long> items = new LinkedList<>();
    private String[] operation;
    private int[] test;
    public int inspections = 0;

    public int testOp() {
        return test[0];
    }

    public void addItem(long item) {
        items.add(item);
    }

    public void setOperation(String[] operation) {
        this.operation = operation;
    }

    public void setTest(int[] test) {
        this.test = test;
    }

    public long inspect(boolean reliefAfterInspect) {
        if (items.isEmpty()) {
            return -1;
        }

        long worry = items.poll();

        String op = operation[0];
        long operand = operation[1].equals("old") ? worry : Long.parseLong(operation[1]);
        switch (op) {
            case "+" -> worry += operand;
            case "*" -> worry *= operand;
        }

        inspections++;

        if (reliefAfterInspect) {
            worry = Math.floorDiv(worry, 3);
        }
        return worry;
    }

    public int test(long item) {
        return item % test[0] == 0 ? test[1] : test[2];
    }
}
