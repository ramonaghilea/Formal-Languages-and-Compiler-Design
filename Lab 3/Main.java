import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
//        SymbolTable symbolTable = new SymbolTable(31);
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

        LexicalScanner lexicalScanner = new LexicalScanner();
        lexicalScanner.parseFile("src/files/p1.txt");
    }
}
