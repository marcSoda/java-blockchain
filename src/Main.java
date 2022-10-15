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
        System.out.println("PROOF OF MEMBERSHIP: ");
        POM pom = bc.balance("bc91428771lkjvlkdjvlzdrv78987698f0b110d4");
        System.out.println(pom);
        FileMan.writeBlockchain(bc, args[0]);
    }
}
