import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.nio.charset.StandardCharsets;

public class Blockchain {
    private byte[] lastHash;

    private List<Block> blocks = new LinkedList<Block>();

    Blockchain() {
        lastHash = "0".getBytes(StandardCharsets.UTF_8);
    }

    public void add(Block block) {
        block.header.prev = lastHash;
        lastHash = block.mine();
        blocks.add(block);
    }

    public boolean verify() {
        Iterator<Block> it = this.iterator();
        byte[] prev = "0".getBytes(StandardCharsets.UTF_8);
        while (it.hasNext()) {
            Block b = it.next();
            if (!Arrays.equals(b.header.prev, prev))
                return false;
            if (!b.verify())
                return false;
            prev = Hash.hash(Util.serialize(b.header));
        }
        return true;
    }

    public POM balance(String address) {
        ListIterator<Block> dit = this.descendingIterator();
        Block foundBlock = null;
        Transaction foundTransaction = null;

        outer:
        while (dit.hasPrevious()) {
            Block b = dit.previous();
            for (Transaction t : b.transactions) {
                if (address.equals(t.address)) {
                    foundBlock = b;
                    foundTransaction = t;
                    break outer;
                }
            }
        }

        if (foundTransaction == null || foundBlock == null) {
            System.out.println("Could not find matching transaction.");
            return null;
        }

        ArrayList<byte[]> merkleProof = MerkleTree.reqProof(foundTransaction, foundBlock.header.root);

        ArrayList<byte[]> blockHashes = new ArrayList<byte[]>();
        while (dit.hasNext()) {
            blockHashes.add(Hash.hash(Util.serialize(dit.next().header)));
        }

        return new POM(
                   foundTransaction.balance,
                   merkleProof,
                   foundBlock.header,
                   blockHashes
        );
    }

    public String toString(boolean showLedger) {
        String print = "";
        for (Block block : this.blocks) {
            print += block.toString(showLedger);
        }
        return print;
    }

    public Iterator<Block> iterator() {
        return blocks.iterator();
    }

    public ListIterator<Block> descendingIterator() {
        return this.blocks.listIterator(this.blocks.size());
    }
}
