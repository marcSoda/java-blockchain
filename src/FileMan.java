import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.IOException;

public class FileMan {
    static byte[][][] readTransactionFiles(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        int numFiles = files.length;
        byte[][][] transactions = new byte[numFiles][][];
        int count = 0;
        for (File file : files) {
            if(file.isFile()) {
                BufferedReader inputStream = null;
                try {
                    inputStream = new BufferedReader(new FileReader(file));
                    Scanner s;
                    ArrayList<String> parts = new ArrayList<String>();
                    try {
                        s = new Scanner(new File(directory + "/" + file.getName()));
                    } catch (Exception e) {
                        System.out.println("input.txt not found");
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
                } catch(IOException e) {
                	System.out.println(e);
                    return null;
                }
                finally {
                    if (inputStream != null) {
                        try { inputStream.close(); }
                        catch (IOException e) { return null; }
                    }
                }
            }
        }
        return transactions;
    }

    static void unzip(String source, String destination) {
        try {
            File destDir = new File(destination);
            if (!destDir.exists()) destDir.mkdir();
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(source));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destination + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private static void extractFile(ZipInputStream zipIn, String filePath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    static void cleanup(String sdir) {
        File dir = new File(sdir);
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File f : contents) {
                f.delete();
            }
        }
        dir.delete();
    }

    static void writeBlockchain(Blockchain bc, String sdir) {
        File dir = new File(sdir);
        if (!dir.exists()) dir.mkdir();
        try {
            //YOU ARE HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEee
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(bc.toString(true));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
