import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.File;
import java.io.Serializable;

class Node implements Serializable {
    Node left;
    Node right;
    byte[] hash;

    Node(byte[] data, Node left, Node right) {
        if (left == null && right == null) {
            this.hash = Hash.hash(data);
        } else {
            byte[] concat = Util.concatBytes(left.hash, right.hash);
            byte[] hash = Hash.hash(concat);
            this.hash = hash;
        }

        this.left = left;
        this.right = right;
    }
}

public class MerkleTree implements Serializable {
    Node root;

    MerkleTree(byte[][] data) {
        ArrayList<Node> leaves = new ArrayList<Node>();

        for (byte[] d : data) {
            leaves.add(new Node(d, null, null));
        }

        if (leaves.size() % 2 != 0) {
            Node last = leaves.get(leaves.size() - 1);
            leaves.add(new Node(last.hash, null, null));
        }

        this.root = this.buildTree(leaves);
    }

    Node buildTree(ArrayList<Node> nodes) {
        if (nodes.size() % 2 != 0) {
            Node last = nodes.get(nodes.size() - 1);
            nodes.add(new Node(last.hash, null, null));
        }

        if (nodes.size() == 2) {
            byte[] hash = Hash.hash(Util.concatBytes(nodes.get(0).hash, nodes.get(1).hash));
            return new Node(hash, nodes.get(0), nodes.get(1));
        }

        Node left = this.buildTree(new ArrayList<>(nodes.subList(0, (nodes.size()) / 2)));
        Node right = this.buildTree(new ArrayList<>(nodes.subList((nodes.size()) / 2, nodes.size())));
        byte[] hash = Hash.hash(Util.concatBytes(left.hash, right.hash));
        return new Node(hash, left, right);
    }

    static void printTree(Node node) {
        if (node != null) {
            if (node.left != null) {
                System.out.println("Left: " + Hash.hex(node.left.hash));
                System.out.println("Right: " + Hash.hex(node.right.hash));
            }
            System.out.println("self: " + Hash.hex(node.hash));
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

        System.out.println(Hash.hex(t.root.hash));
    }
}
