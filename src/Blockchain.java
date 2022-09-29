import java.util.LinkedList;
import java.util.List;
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
