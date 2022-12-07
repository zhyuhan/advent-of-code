import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day07 {
    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("2022/day07/input.in");
        Scanner input = new Scanner(inputFile);

        Directory filesystem = new Directory("/");
        Directory curr = filesystem;

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.startsWith("$")) {
                String[] command = line.split(" ");
                if (command[1].equals("ls")) continue;

                String directory = command[2];
                switch (directory) {
                    case "/" -> {
                    }
                    case ".." -> curr = curr.parent;
                    default -> curr = curr.getChild(directory);

                }
            } else if (line.startsWith("dir")) {
                String directory = line.split(" ")[1];
                Directory dir = new Directory(directory);
                dir.parent = curr;
                curr.addChild(dir);
            } else {
                int fileSize = Integer.parseInt(line.split(" ")[0]);
                curr.addFile(fileSize);
            }
        }

        // part 1
        System.out.println(filesystem.toDeleteSize());

        // part 2
        System.out.println(freeableSize(filesystem, 30000000 - (70000000 - filesystem.totalSize())));

        input.close();
    }

    static int freeableSize(Directory directory, int target) {
        if (directory.totalSize() < target) {
            return Integer.MAX_VALUE;
        }

        int min = directory.totalSize();
        for (Directory dir : directory.children) {
            min = Math.min(min, freeableSize(dir, target));
        }
        return min;
    }
}

class Directory {
    public String name;
    private int size;
    public Directory parent;
    public final List<Directory> children = new ArrayList<>();

    Directory(String n) {
        name = n;
    }

    public Directory getChild(String name) {
        for (Directory dir : children) {
            if (dir.name.equals(name)) {
                return dir;
            }
        }
        return null;
    }

    public void addFile(int fileSize) {
        size += fileSize;
    }

    public void addChild(Directory child) {
        children.add(child);
    }

    public int totalSize() {
        int totalSize = size;
        for (Directory child : children) {
            totalSize += child.totalSize();
        }
        return totalSize;
    }

    public boolean canBeDeleted() {
        return totalSize() <= 100000;
    }

    public int toDeleteSize() {
        int total = canBeDeleted() ? totalSize() : 0;
        for (Directory dir : children) {
            total += dir.toDeleteSize();
        }
        return total;
    }
}
