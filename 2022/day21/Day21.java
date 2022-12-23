import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day21 {
    static Map<String, TreeMonkey> monkeys = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day21/input.in");
        Scanner input = new Scanner(inputFile);

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.equals("")) continue;

            String[] tokens = line.split(" ");
            String name = tokens[0].substring(0, 4);
            TreeMonkey monkey;
            if (tokens.length == 2) {
                monkey = new TreeMonkey(Long.parseLong(tokens[1]));
            } else {
                String left = tokens[1];
                char op = tokens[2].charAt(0);
                String right = tokens[3];
                monkey = new TreeMonkey(left, op, right);
            }
            monkeys.put(name, monkey);
        }
        input.close();

        // part 1
        System.out.println(TreeMonkey.doJob("root", monkeys));

        // part 2
        TreeMonkey root = monkeys.get("root");
        if (TreeMonkey.containsHumn(root.left, monkeys)) {
            long otherResult = TreeMonkey.doJob(root.right, monkeys);
            solveForHumn(root.left, otherResult);
        } else {
            long otherResult = TreeMonkey.doJob(root.left, monkeys);
            solveForHumn(root.right, otherResult);
        }
    }

    static void solveForHumn(String name, long nextResult) {
        if (name.equals("humn")) {
            System.out.println(nextResult);
        }

        TreeMonkey monkey = monkeys.get(name);

        if (monkey.isLeaf) return;

        boolean isInLeft = TreeMonkey.containsHumn(monkey.left, monkeys);

        long other = TreeMonkey.doJob(isInLeft ? monkey.right : monkey.left, monkeys);
        char op = monkey.op;
        switch (op) {
            case '+' -> nextResult -= other;
            case '-' -> {
                if (isInLeft) {
                    nextResult += other;
                } else {
                    nextResult = other - nextResult;
                }
            }
            case '*' -> nextResult /= other;
            case '/' -> {
                if (isInLeft) {
                    nextResult *= other;
                } else {
                    nextResult = other / nextResult;
                }
            }
        }
        solveForHumn(isInLeft ? monkey.left : monkey.right, nextResult);
    }
}

class TreeMonkey {
    public char op;
    public long value;
    public boolean isLeaf = false;
    public String left;
    public String right;

    TreeMonkey(long n) {
        value = n;
        isLeaf = true;
    }

    TreeMonkey(String l, char o, String r) {
        left = l;
        op = o;
        right = r;
    }

    static boolean containsHumn(String name, Map<String, TreeMonkey> monkeys) {
        if (name.equals("humn")) {
            return true;
        }

        TreeMonkey monkey = monkeys.get(name);
        return !monkey.isLeaf && (TreeMonkey.containsHumn(monkey.left, monkeys) || TreeMonkey.containsHumn(monkey.right, monkeys));
    }

    static long doJob(String name, Map<String, TreeMonkey> monkeys) {
        TreeMonkey monkey = monkeys.get(name);

        if (monkey.isLeaf) {
            return monkey.value;
        }

        long left = doJob(monkey.left, monkeys);
        long right = doJob(monkey.right, monkeys);

        switch (monkey.op) {
            case '+' -> {
                return left + right;
            }
            case '-' -> {
                return left - right;
            }
            case '*' -> {
                return left * right;
            }
            default -> {
                return left / right;
            }
        }
    }
}
