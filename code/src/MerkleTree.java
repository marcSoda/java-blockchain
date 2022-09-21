package code.src;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;

public class MerkleTree {

    public MerkleTree() {
        
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

    void buildTree() {

    }

    void printTree() {

    }

    void getRootHash() {

    }
}

