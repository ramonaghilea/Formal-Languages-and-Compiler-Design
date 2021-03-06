import Parser.Grammar;
import Parser.LL1Parser;
import Parser.ParserOutput;

import java.util.List;

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
//        lexicalScanner.scanProgramFile("src/files/lexicalScanner/p1.txt");


//        FiniteAutomata finiteAutomata = new FiniteAutomata("src/files/finiteAutomata/FA.in");
//        System.out.println(finiteAutomata);
//
//        System.out.println(finiteAutomata.verifyAcceptedSequence(""));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("011"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("0"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("010"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("0111"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("01111"));

//        FiniteAutomata finiteAutomata = new FiniteAutomata("src/files/finiteAutomata/NDFA.in");
//        System.out.println(finiteAutomata);
//
//        System.out.println(finiteAutomata.verifyAcceptedSequence("01"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("011"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("0"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("010"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("0111"));
//        System.out.println(finiteAutomata.verifyAcceptedSequence("01111"));

//        FiniteAutomata identifierFiniteAutomata = new FiniteAutomata(
//        "src/files/finiteAutomata/identifierFA.in");
//        System.out.println(identifierFiniteAutomata);
//
//        System.out.println(identifierFiniteAutomata.verifyAcceptedSequence("ABC"));
//        System.out.println(identifierFiniteAutomata.verifyAcceptedSequence("A123"));
//        System.out.println(identifierFiniteAutomata.verifyAcceptedSequence("0"));
//        System.out.println(identifierFiniteAutomata.verifyAcceptedSequence("10BCD"));
//        System.out.println(identifierFiniteAutomata.verifyAcceptedSequence("_ABC"));
//        System.out.println(identifierFiniteAutomata.verifyAcceptedSequence("_189"));

//        FiniteAutomata integerConstantFiniteAutomata = new FiniteAutomata(
//                "src/files/finiteAutomata/integerConstantFA.in");
//        System.out.println(integerConstantFiniteAutomata);
//
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("120"));
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("-5"));
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("0"));
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("-0"));
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("-08"));
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("-100"));
//        System.out.println(integerConstantFiniteAutomata.verifyAcceptedSequence("-100A"));

//        UI ui = new UI(finiteAutomata);
//        System.out.println(ui.getFAAsString());
        //ui.run();

        Grammar g2 = new Grammar("src/Parser/files/g2.txt");
        System.out.println(g2);
        System.out.println("Is CFG: " + g2.isCFG());
        System.out.println(g2.getProductionsForNonterminal("program"));
//
        Grammar g1 = new Grammar("src/Parser/files/g1.txt");
        System.out.println(g1);
        System.out.println("Is CFG: " + g1.isCFG());

        Grammar g3 = new Grammar("src/Parser/files/g3.txt");
        System.out.println(g3);
        System.out.println("Is CFG: " + g3.isCFG());

        //UI parserUI = new UI(g1);
        //parserUI.run();

        LL1Parser ll1Parserg3 = new LL1Parser(g3, "src/Parser/files/seq.txt", "src/Parser/files/out3.txt");
//        List<Integer> output = ll1Parser.analyseSequence("( int ) + int");
//
//        ParserOutput parserOutput = new ParserOutput(output, ll1Parser.getNumberToProduction(), g3);


        //LL1Parser ll1Parser = new LL1Parser(g1);
        // List<Integer> output = ll1Parser.analyseSequence("a c a");

//        ParserOutput parserOutput = new ParserOutput(output, ll1Parser.getNumberToProduction(), g3);
//        parserOutput.printToConsole();
//        parserOutput.printToFile("src/Parser/files/out1.txt");

        LL1Parser ll1Parserg2 = new LL1Parser(g2, "src/Parser/files/PIF.out", "src/Parser/files/out2.txt");
        //ll1Parser.analyseSequence("START identifier : INT ; BEGIN READ ( identifier ) ; END ENDPRG");
//
        //ParserOutput parserOutput = new ParserOutput(output, ll1Parser.getNumberToProduction(), g2);
    }
}
