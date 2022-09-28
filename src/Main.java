class Main {
    public static void main(String args[]) {
        FileMan.unzip("blocks.zip", "blocks");
        byte[][][] transactions = FileMan.readTransactionFiles("blocks");
        Blockchain bc = new Blockchain(Block.defaultGenesis());
        int ctr = 0;
        for (byte[][] t : transactions) {
            Block b = new Block(t, bc.blocks.get(ctr).header.hash);
            bc.add(b);
            ctr++;
        }
        System.out.println(bc.toString(true));
        // FileMan.writeBlockchain(bc, "block_out");
        FileMan.cleanup("blocks");
    }
}
