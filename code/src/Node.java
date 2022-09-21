package code.src;

import java.security.NoSuchAlgorithmException;

public class Node {
    private Node left;
    private Node right;
    private String hash;

    public Node (Node left, Node right, String hash) {
        this.left = left;
        this.right = right;
        this.hash = hash;
    }
    public Node getLeftNode() {
        return left;
    }
    public void setLeftNode(Node left) {
        this.left = left;
    }
    public Node getRightNode() {
        return right;
    }
    public void setRightNode(Node right) {
        this.right = right;
    }
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void dupe() {
        new Node(this.left, this.right, this.hash);
    }
    public String hash(String str) {
        String ret = "";
        try {
            byte[] arr = GFG2.getSHA(str);
            ret = GFG2.toHexString(arr);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return ret;
    }
}