import java.math.BigInteger;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;

public class Block {
    static int difficulty = 12; //1 produces a target of exaclty (2^256)/2
    BlockHeader header;
    Transaction[] transactions;
    MerkleTree tree;

    Block(Transaction[] transactions) {
        this.transactions = transactions;
        this.header = new BlockHeader();
        this.header.timestamp = Instant.now().toString();
        this.header.target = BigInteger.valueOf(1).shiftLeft(256 - difficulty);
        this.header.root = this.generateRoot();
    }

    Node generateRoot() {
        return new MerkleTree(this.transactions).root;
    }

    byte[] mine() {
        BigInteger intHash;
        byte[] hash = null;
        this.header.nonce = 0;
        while (this.header.nonce < Long.MAX_VALUE) {
            byte[] ser = Util.serialize(this.header);
            hash = Hash.hash(ser);
            System.out.printf("\rNonce: %d | Hash: %s ", this.header.nonce, Hash.hex(hash));
            intHash = new BigInteger(1, hash);
            if (intHash.compareTo(this.header.target) == -1) break;
            this.header.nonce++;
        }
        System.out.println();
        return hash;
    }

    boolean verify() {
        if (!Arrays.equals(this.header.root.hash, this.generateRoot().hash))
            return false;
        byte[] hash = Hash.hash(Util.serialize(this.header));
        BigInteger intHash = new BigInteger(1, hash);
        if (intHash.compareTo(this.header.target) == -1)
            return true;
        return false;
    }

    static Block defaultGenesis() {
        Transaction[] transactions = new Transaction[0];
        return new Block(transactions);
    }

    public String toString(boolean showLedger) {
        String begin = "BEGIN BLOCK\n";
        String end = "END BLOCK\n";
        String header = this.header.toString();
        if (!showLedger) return begin + header + end;
        String transactions = "\tTransactions:\n";
        for (Transaction t : this.transactions)
            transactions += "\t\t" + t.address + " " + t.balance + "\n";
        return begin + header + transactions + end;
    }
}

class BlockHeader implements Serializable {
    byte[] prev;
    Node root;
    String timestamp;
    BigInteger target;
    long nonce;

    public String toString() {
        String prev = Hash.hex(this.prev);
        String root = Hash.hex(this.root.hash);
        return "\tBEGIN HEADER\n" +
               "\t\tPrevious Hash:    " + prev      + "\n" +
               "\t\tMerkle Root Hash: " + root      + "\n" +
               "\t\tTimestamp:        " + timestamp + "\n" +
               "\t\tTarget:           " + target    + "\n" +
               "\t\tNonce:            " + nonce     + "\n" +
               "\tEND HEADER\n";
    }
}
