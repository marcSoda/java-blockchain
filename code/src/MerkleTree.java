import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

class Node {
    Node left;
    Node right;
    byte[] data;

    Node(byte[] data, Node left, Node right) {
        if (left == null && right == null) {
            this.data = hash(data);
        } else {
            byte[] concat = new byte[left.data.length + right.data.length];
            for (int i = 0; i < concat.length; ++i)
                concat[i] = i < left.data.length ? left.data[i] : right.data[i - left.data.length];
            byte[] hash = hash(concat);
            this.data = hash;
        }

        this.left = left;
        this.right = right;
    }

    static byte[] hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (Exception e) {
            System.out.println("Failed to get instance of sha256");
            return null;
        }
    }

	static String toHexString(byte[] hash) {
		BigInteger number = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));
		while (hexString.length() < 64) hexString.insert(0, '0');
		return hexString.toString();
	}
}

public class MerkleTree {
    Node root;

    MerkleTree(byte[][] data) {
        ArrayList<Node> nodes = new ArrayList<Node>();

        if (data.length % 2 != 0) {
            byte[][] newData = new byte[data.length + 1][256];
            newData[newData.length - 1] = data[data.length - 1];
            data = newData;
        }

        for (byte[] d : data) {
            Node n = new Node(d, null, null);
            nodes.add(n);
        }

        for (int i = 0; i < data.length / 2; i++) {
            ArrayList<Node> level = new ArrayList<Node>();
            for (int j = 0; j < nodes.size(); j+=2) {
                System.out.println(nodes);
                System.out.println(i + " " + j);
                Node n = new Node(null, nodes.get(j), nodes.get(j+1));
                level.add(n);
            }
            nodes = level;
        }
        this.root = nodes.get(0);
    }

    public static void main(String args[]) {
        byte[] d0 = Node.hash(("23uhf0f789haw40uh" + "24").getBytes(StandardCharsets.UTF_8));
        byte[] d1 = Node.hash(("colih34o897hfdq3j" + "908429").getBytes(StandardCharsets.UTF_8));
        byte[] d2 = Node.hash(("lfkhjsoiufhe4507h" + "99").getBytes(StandardCharsets.UTF_8));
        byte[] d3 = Node.hash(("lvkjdhglddd589t4f" + "8").getBytes(StandardCharsets.UTF_8));
        byte[] d4 = Node.hash(("flksjhrfoi4rhfs34" + "123433").getBytes(StandardCharsets.UTF_8));
        byte[] d5 = Node.hash(("f348937fheo87fwfh" + "23").getBytes(StandardCharsets.UTF_8));
        byte[] d6 = Node.hash(("dfsufhoety9we484r" + "7").getBytes(StandardCharsets.UTF_8));
        byte[][] data = new byte[6][];
        data[0] = d0;
        data[1] = d1;
        data[2] = d2;
        data[3] = d3;
        data[4] = d4;
        data[5] = d5;
        MerkleTree t = new MerkleTree(data);
    }
}
