import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day05/input.in");
        Scanner input = new Scanner(inputFile);

        List<Stack<Character>> stacks = new ArrayList<>();

        List<String> stack_lines = new ArrayList<>();
        Pattern move_regex = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

        while (input.hasNextLine()) {
            String line = input.nextLine();

            Matcher m = move_regex.matcher(line);
            if (m.matches()) {
                int num = Integer.parseInt(m.group(1));
                int from = Integer.parseInt(m.group(2)) - 1;
                int to = Integer.parseInt(m.group(3)) - 1;
                
                Stack<Character> move_stack = new Stack<>();
                for (int i = 0; i < num; i++) {
                    char item = stacks.get(from).pop();
                    
                    // part 1
                    // stacks.get(to).push(item);
                    
                    // part 2
                    move_stack.push(item);
                }
                
                // part 2
                for (int i = 0; i < num; i++) {
                    stacks.get(to).push(move_stack.pop());
                }
            } else if (line.matches("\\s(\\d+\\s*)+")) {
                int num_stacks = (line.length() + 1) / 4;
                for (int i = 0; i < num_stacks; i++) {
                    stacks.add(new Stack<>());
                }
                for (int i = stack_lines.size() - 1; i >= 0; i--) {
                    String s = stack_lines.get(i);
                    for (int j = 1; j < s.length(); j += 4) {
                        char item = s.charAt(j);
                        if (item != ' ') {
                            stacks.get((j - 1) / 4).push(item);
                        }
                    }
                }
                System.out.println(stacks.size());
            } else {
                stack_lines.add(line);
            }
        }
        input.close();
        
        for (Stack<Character> stack : stacks) {
            System.out.print(stack.peek());
        }
        System.out.println();
    }
}
