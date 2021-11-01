public class Main {
    public static void main(String[] args) {
//        SymbolTable symbolTable = new SymbolTable();
//
//        symbolTable.add("a"); // position 4
//        symbolTable.add("bcd"); // position 18
//        // this should generate a collision because h("bcd") = h("add")
//        // "add" is added on position 19
//        symbolTable.add("add");
//        System.out.println(symbolTable.add("a")); // this should return false, because "a" already exists in the table
//
//        System.out.println(symbolTable.search("a")); // 4
//        System.out.println(symbolTable.search("bcd")); // 18
//        System.out.println(symbolTable.search("add")); // 19

//        LexicalScanner lexicalScanner = new LexicalScanner();
//        lexicalScanner.scanFile("src/files/p1.txt");

//        LexicalScanner lexicalScanner = new LexicalScanner();
//        lexicalScanner.scanProgramFile("src/files/lexicalScanner/p1.txt");

        FiniteAutomata finiteAutomata = new FiniteAutomata();
        finiteAutomata.readFile("src/files/finiteAutomata/FA.in");
        // System.out.println(finiteAutomata.toString());

        UI ui = new UI(finiteAutomata);
        ui.run();
    }
}
