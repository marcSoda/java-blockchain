import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.ListIterator;
import java.io.IOException;

public class FileMan {
    static Transaction[][] readTransactionFiles(String[] fnames) {
        Transaction[][] transactions = new Transaction[fnames.length][];
        int count = 0;
        for (String fname : fnames) {
            File file = new File(fname);
            if (!file.exists()) {
                System.out.println("Fatal error: " + fname + " does not exist");
                System.exit(1);
            }
            try {
                Scanner s;
                ArrayList<String> lines = new ArrayList<String>();
                try {
                    s = new Scanner(file);
                } catch (Exception e) {
                    System.out.println("Fatal error: failed to initialize scanner for" + fname);
                    System.exit(1);
                    return null;
                }
                while (s.hasNextLine())
                    lines.add(s.nextLine());
                s.close();

                Transaction[] data = new Transaction[lines.size()];
                for (int i = 0; i < lines.size(); i++) {
                    String[] split = lines.get(i).split(" ");
                    Transaction t = new Transaction(split[0], split[1]);
                    data[i] = t;
                }
                transactions[count] = data;
                count++;
            } catch(Exception e) {
                System.out.println("Fatal error: " + fname + " is an invalid transaction list");
                System.exit(1);
            }
        }
        return transactions;
    }

    static void writeBlockchain(Blockchain bc, String fname) {
        int firstIdx = 0;
        if (fname.contains("/")) firstIdx = fname.lastIndexOf('/') + 1;
        int lastIdx = fname.length();
        if (fname.contains(".")) lastIdx = fname.lastIndexOf('.');
        String outName = fname.substring(firstIdx, lastIdx) + ".block.out";
        ListIterator<Block> it = bc.descendingIterator();
        try {
            File outFile = new File(outName);
            outFile.delete();
            outFile.createNewFile();
            FileWriter w = new FileWriter(outFile);
            while (it.hasPrevious()) {
                w.write(it.previous().toString(true));
            }
            w.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Fatal error: IOException when writing blockchain");
            System.exit(1);
        }
    }
}
