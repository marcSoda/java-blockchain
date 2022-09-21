package code.src;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String args[]) {
        Scanner inp = new Scanner(System.in);
        if (inp.hasNext()) {
            try {
                File file = new File(inp.next());
                Scanner filescan = new Scanner(file);
                while (filescan.hasNextLine()) {
                    String[] tokens = filescan.nextLine().split(" ");
                    for (int i = 0; i < tokens.length; i++) {
                        System.out.println(tokens[i]);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please input a valid filelocation.");
            }
        }
    }
}