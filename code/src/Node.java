package src;

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
}