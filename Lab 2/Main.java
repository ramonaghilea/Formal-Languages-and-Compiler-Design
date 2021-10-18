public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(31);

        symbolTable.add("a");
        symbolTable.add("bcd");
        symbolTable.add("abd");
        System.out.println(symbolTable.add("a"));

        System.out.println(symbolTable.search("a"));
        System.out.println(symbolTable.search("bcd"));
        System.out.println(symbolTable.search("abd"));
    }
}
