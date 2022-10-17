import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.ListIterator;
import java.io.IOException;
import java.math.BigInteger;

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
                try {
                    s = new Scanner(file);
                } catch (Exception e) {
                    System.out.println("Fatal error: failed to initialize scanner for" + fname);
                    System.exit(1);
                    return null;
                }

                Transaction[] data = getLedger(s);
                s.close();

                transactions[count] = data;
                count++;
            } catch (Exception e) {
                System.out.println("Fatal error: " + fname + " is an invalid transaction list");
                System.exit(1);
            }
        }
        return transactions;
    }

    private static Transaction[] getLedger(Scanner s) {
        ArrayList<String> lines = new ArrayList<String>();
        while (s.hasNextLine()) {
            String l = s.nextLine().trim();
            if (l.equals("END TRANSACTIONS"))
                break;
            lines.add(l);
        }

        Transaction[] ledger = new Transaction[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String[] split = lines.get(i).split(" ");
            Transaction t = new Transaction(split[0], split[1]);
            ledger[i] = t;
        }
        return ledger;
    }

    static void writeBlockchain(Blockchain bc, String fname) {
        int firstIdx = 0;
        if (fname.contains("/"))
            firstIdx = fname.lastIndexOf('/') + 1;
        int lastIdx = fname.length();
        if (fname.contains("."))
            lastIdx = fname.lastIndexOf('.');
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
            System.out.println("Fatal error: IOException when writing blockchain");
            System.exit(1);
        }
    }

    static Blockchain readBlockchain(String path) {
        ArrayList<Block> bl = new ArrayList<Block>();
        try {
            Scanner s = new Scanner(new File(path));
            while (s.hasNextLine()) {
                bl.add(readBlock(s));
            }
            s.close();
        } catch (Exception e) {
            System.out.println("Failed to read blockchain file.");
            System.out.println(e);
            System.exit(1);
        }
        return new Blockchain(bl);
    }

    private static Block readBlock(Scanner s) {
        String l = s.nextLine().trim();
        if (!l.equals("BEGIN BLOCK")) {
            System.out.println("Fatal: Bad block: Could not find beginning of block.");
            System.exit(1);
        }
        while (s.hasNextLine()) {
            l = s.nextLine().trim();
            if (l.equals("END BLOCK"))
                break;
            BlockHeader head = new BlockHeader();
            while (s.hasNextLine()) {
                l = s.nextLine().trim();
                if (l.equals("END HEADER"))
                    break;
                String[] split = l.split("\\s+");

                switch (split[0]) {
                    case "PreviousHash:":
                        head.prev = Hash.hexToHash(split[1]);
                        break;
                    case "MerkleRootHash:":
                        head.rootHash = Hash.hexToHash(split[1]);
                        break;
                    case "Timestamp:":
                        head.timestamp = split[1];
                        break;
                    case "Target:":
                        head.target = new BigInteger(split[1]);
                        break;
                    case "Nonce:":
                        head.nonce = Long.parseLong(split[1]);
                        break;
                    default:
                        System.out.println("Fatal Error when parsing block header.");
                        System.exit(1);
                }
            }
            if (!s.nextLine().trim().equals("BEGIN TRANSACTIONS")) {
                System.out.println("Fatal Error: Bad Block: Could not find beginning of ledger.");
                System.exit(1);
            }
            Transaction[] ledger = getLedger(s);
            if (!s.nextLine().trim().equals("END BLOCK")) {
                System.out.println("Fatal Error: Bad Block: Could not find end of block.");
                System.exit(1);
            }

            return new Block(head, ledger);
        }
        return null;
    }
}
