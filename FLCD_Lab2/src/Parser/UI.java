package Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UI {
    private Grammar grammar;

    public UI(Grammar grammar) {
        this.grammar = grammar;
    }

    private void displayMenu() {
        String menu = "\n";
        menu += "0. Exit.\n";
        menu += "1. Display the set of nonterminals.\n";
        menu += "2. Display the set of terminals.\n";
        menu += "3. Display the productions.\n";
        menu += "4. Display the productions for a given nonterminal.\n";
        menu += "5. Display grammar details.\n";
        menu += "6. Verify if the grammar is CFG.\n";

        System.out.println(menu);
    }

    private String readStringFromConsole() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String string = "";

        System.out.print("Enter string : ");
        try {
            string = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return string;
    }

    private String getNonterminalsAsString() {
        String result = "Nonterminals = { ";
        for(String nonterminal: this.grammar.getNonterminals())
            result += nonterminal + " ";
        result += "}";

        return result;
    }

    private String getTerminalsAsString() {
        String result = "Terminals = { ";
        for(String element: this.grammar.getTerminals())
            result += element + " ";
        result += "}";

        return result;
    }

    private String getRhsElementAsString(List<String> rhsElement) {
        String result = "";
        for(int i = 0; i < rhsElement.size() - 1; i++)
            result += rhsElement.get(i) + " ";
        result += rhsElement.get(rhsElement.size() - 1);

        return result;
    }

    private String getProductionsAsString() {
        String result = "Productions = { \n";
        for(List<String> element: this.grammar.getProductions().keySet()) {
            for(int i = 0; i < element.size() - 1; i++)
                result += element.get(i) + " ";
            result += element.get(element.size() - 1);

            result += element + " -> " + this.grammar.getProductionsForNonterminal(element.get(0)).get(0);
            for(int i = 1; i < this.grammar.getProductionsForNonterminal(element.get(0)).size(); i++)
                result += " | " + getRhsElementAsString(this.grammar.getProductionsForNonterminal(element.get(0)).get(i));
            result += "\n";
        }
        result += "}";

        return result;
    }

    private String getProductionsForNonterminalAsString() {
        String nonterminal = readStringFromConsole();
        List<List<String>> productionsForNonterminal = this.grammar.getProductionsForNonterminal(nonterminal);

        String result = nonterminal + " -> ";
        for(int i = 1; i < productionsForNonterminal.size() - 1; i++) {
            result += getRhsElementAsString(productionsForNonterminal.get(i)) + " | ";
        }
        result += getRhsElementAsString(productionsForNonterminal.get(productionsForNonterminal.size() - 1));
        result += "\n";

        return result;
    }

    public String getGrammarAsString() {
        String result = getNonterminalsAsString() + "\n" +
                getTerminalsAsString() + "\n" +
                "Starting symbol = " + this.grammar.getStartingSymbol() + "\n" +
                getProductionsAsString() + "\n";

        return result;
    }

    private void verifyCFG() {
        if(this.grammar.isCFG())
            System.out.println("The grammar is a context free grammar.");
        else
            System.out.println("The grammar is NOT a context free grammar.");
    }

    public void run() {
        boolean running = true;
        String command = "0";

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(running) {
            displayMenu();
            System.out.print("Enter command : ");
            try {
                command = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (command) {
                case "0" -> running = false;
                case "1" -> System.out.println(getNonterminalsAsString());
                case "2" -> System.out.println(getTerminalsAsString());
                case "3" -> System.out.println(getProductionsAsString());
                case "4" -> System.out.println(getProductionsForNonterminalAsString());
                case "5" -> System.out.println(getGrammarAsString());
                case "6" -> verifyCFG();
            }
        }
    }
}
