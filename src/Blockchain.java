import java.util.LinkedList;
import java.util.List;

public class Blockchain {
    public List<Block> blocks = new LinkedList<Block>();

    Blockchain(Block genesis) {
        this.add(genesis);
    }

    public void add(Block block) {
        blocks.add(block);
    }

    public String toString(boolean showLedger) {
        String print = "";
        for (Block block : this.blocks) {
            print += block.toString(showLedger);
        }
        return print;
    }
}
