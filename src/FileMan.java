import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.ListIterator;
import java.io.IOException;

public class FileMan {
    static byte[][][] readTransactionFiles(String[] fnames) {
        byte[][][] transactions = new byte[fnames.length][][];
        int count = 0;
        for (String fname : fnames) {
            File file = new File(fname);
            if (!file.exists()) {
                System.out.println("Fatal error: " + fname + " does not exist");
                System.exit(1);
            }
            BufferedReader inputStream = null;
            try {
                inputStream = new BufferedReader(new FileReader(file));
                Scanner s;
                ArrayList<String> parts = new ArrayList<String>();
                try {
                    s = new Scanner(file);
                } catch (Exception e) {
                    System.out.println("Fatal error: failed to initialize scanner for" + fname);
                    System.exit(1);
                    return null;
                }
                while (s.hasNext()){
                    parts.add(s.next());
                }
                s.close();
                byte[][] data = new byte[parts.size() / 2][];

                for (int i = 0; i < parts.size(); i+=2) {
                    byte[] transConcat = (parts.get(i) + " " + parts.get(i+1)).getBytes(StandardCharsets.UTF_8);
                    data[i/2] = transConcat;
                }
                transactions[count] = data;
                count++;
            } catch(Exception e) {
                System.out.println("Fatal error: " + fname + " is an invalid transaction list");
                System.exit(1);
            }
            finally {
                if (inputStream != null) {
                    try { inputStream.close(); }
                    catch (IOException e) {
                        System.out.println("Fatal error: " + fname + " caused in IOException");
                        System.exit(1);
                    }
                }
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
