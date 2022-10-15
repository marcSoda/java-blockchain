class Main {
    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("No input files");
            return;
        }
        Transaction[][] transactions = FileMan.readTransactionFiles(args);
        Blockchain bc = new Blockchain();
        for (Transaction[] t : transactions) {
            Block b = new Block(t);
            bc.add(b);
        }
        System.out.println(bc.toString(true));
        FileMan.writeBlockchain(bc, args[0]);
    }
}
