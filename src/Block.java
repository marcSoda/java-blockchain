import java.math.BigInteger;
import java.io.Serializable;
import java.time.Instant;
import java.nio.charset.StandardCharsets;

public class Block implements Serializable {
    static int difficulty = 16;
    BlockHeader header;
    byte[][] transactions;

    Block(byte[][] data, byte[] prevHash) {
        this.transactions = data;
        this.header = new BlockHeader();
        this.header.timestamp = Instant.now().toString();
        this.header.root = this.getRootHash();
        this.header.prev = prevHash;
        this.header.target = BigInteger.valueOf(1).shiftLeft(256 - difficulty);
        this.header.hash = this.generate();
    }

    byte[] getRootHash() {
        MerkleTree t = new MerkleTree(this.transactions);
        return t.root.hash;
    }

    byte[] generate() {
        BigInteger intHash;
        byte[] hash = null;
        this.header.nonce = 0;
        while (this.header.nonce < Long.MAX_VALUE) {
            byte[] ser = Util.serialize(this);
            hash = Hash.hash(ser);
            System.out.printf("\rNonce: %d | Hash: %s ", this.header.nonce, Hash.hex(hash));
            intHash = new BigInteger(1, hash);
            if (intHash.compareTo(this.header.target) == -1) break;
            this.header.nonce++;
        }
        System.out.println();
        return hash;
    }

    static Block defaultGenesis() {
        byte[][] transactions = { "Genesis".getBytes(StandardCharsets.UTF_8) };
        byte[] prevHash = "0".getBytes(StandardCharsets.UTF_8);
        return new Block(transactions, prevHash);
    }

    public String toString(boolean showLedger) {
        String begin = "BEGIN BLOCK\n";
        String end = "\nEND BLOCK\n\n";
        String header = this.header.toString();
        if (!showLedger) return begin + header + end;
        String transactions = "Transactions:\n";
        for (byte[] transaction : this.transactions) {
            transactions += "\t" + new String(transaction, StandardCharsets.UTF_8);
            transactions += "\n";
        }
        return begin + header + transactions + end;
    }
}

class BlockHeader implements Serializable {
    byte[] prev;
    byte[] hash;
    byte[] root;
    String timestamp;
    BigInteger target;
    long nonce;

    public String toString() {
        String prev = Hash.hex(this.prev);
        String hash = Hash.hex(this.hash);
        String root = Hash.hex(this.root);
        return "\tBEGIN HEADER\n" +
               "\t\tPrevious Hash:    " + prev      + "\n" +
               "\t\tCurrent Hash:     " + hash      + "\n" +
               "\t\tMerkle Root Hash: " + root      + "\n" +
               "\t\tTimestamp Hash:   " + timestamp + "\n" +
               "\t\tTarget:           " + target    + "\n" +
               "\t\tNonce:            " + nonce     + "\n" +
               "\tEND HEADER\n";
    }
}
