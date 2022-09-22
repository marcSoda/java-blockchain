import java.util.ArrayList;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.io.File;

class Node {
    Node left;
    Node right;
    byte[] data;

    Node(byte[] data, Node left, Node right) {
        if (left == null && right == null) {
            this.data = hash(data);
        } else {
            byte[] concat = concat(left.data, right.data);
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

    static String hexString(byte[] hash) {
	BigInteger number = new BigInteger(1, hash);
	StringBuilder hexString = new StringBuilder(number.toString(16));
	while (hexString.length() < 64) hexString.insert(0, '0');
        return hexString.toString();
    }

    static byte[] concat(byte[] l, byte[] r) {
        byte[] concat = new byte[l.length + r.length];
        for (int i = 0; i < concat.length; ++i)
            concat[i] = i < l.length ? l[i] : r[i - l.length];
        return concat;
    }
}

public class MerkleTree {
    Node root;

    MerkleTree(byte[][] data) {
        ArrayList<Node> leaves = new ArrayList<Node>();

        for (byte[] d : data) {
            leaves.add(new Node(d, null, null));
        }

        if (leaves.size() % 2 != 0) {
            Node last = leaves.get(leaves.size() - 1);
            leaves.add(new Node(last.data, null, null));
        }

        this.root = this.buildTree(leaves);
    }

    Node buildTree(ArrayList<Node> nodes) {
        if (nodes.size() % 2 != 0) {
            Node last = nodes.get(nodes.size() - 1);
            nodes.add(new Node(last.data, null, null));
        }

        if (nodes.size() == 2) {
            byte[] hash = Node.hash(Node.concat(nodes.get(0).data, nodes.get(1).data));
            return new Node(hash, nodes.get(0), nodes.get(1));
        }

        Node left = this.buildTree(new ArrayList<>(nodes.subList(0, (nodes.size()) / 2)));
        Node right = this.buildTree(new ArrayList<>(nodes.subList((nodes.size()) / 2, nodes.size())));
        byte[] hash = Node.hash(Node.concat(left.data, right.data));
        return new Node(hash, left, right);
    }

    static void printTree(Node node) {
        if (node != null) {
            if (node.left != null) {
                System.out.println("Left: " + Node.hexString(node.left.data));
                System.out.println("Right: " + Node.hexString(node.right.data));
            }
            System.out.println("self: " + Node.hexString(node.data));
            printTree(node.left);
            printTree(node.right);
        }
    }

    public static void main(String args[]) {
        Scanner s;
        ArrayList<String> parts = new ArrayList<String>();
        try {
            s = new Scanner(new File("./input.txt"));
        } catch (Exception e) {
            System.out.println("input.txt not found");
            return;
        }
        while (s.hasNext()){
            parts.add(s.next());
        }
        s.close();

        byte[][] data = new byte[parts.size() / 2][];

        for (int i = 0; i < parts.size(); i+=2) {
            byte[] transConcat = (parts.get(i) + parts.get(i+1)).getBytes(StandardCharsets.UTF_8);
            data[i/2] = transConcat;
        }

        MerkleTree t = new MerkleTree(data);

	System.out.println(Node.hexString(t.root.data));
    }
}
