package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    private List<String> nonterminals;
    private List<String> terminals;
    private String startingSymbol;
    private Map<String, List<String>> productions;

    private String fileName;

    public Grammar(String fileName) {
        this.nonterminals = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.productions = new HashMap<>();
        this.fileName = fileName;
        readFile();
    }

    public void readFile() {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        nonterminals = readLine(scanner.nextLine());
        terminals = readLine(scanner.nextLine());
        startingSymbol = readLine(scanner.nextLine()).get(0);

        // read the productions
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            readProduction(line);
        }
    }

    private List<String> readLine(String line) {
        String[] lineElements = line.split(" ");
        return Arrays.asList(lineElements);
    }

    private void readProduction(String line) {
        String[] lineElements = line.split(" -> ");
        String[] rhsElements = lineElements[1].split(" \\| ");
        if(this.productions.containsKey(lineElements[0])) {
            this.productions.get(lineElements[0]).addAll(List.of(rhsElements));
        }
        else
            this.productions.put(lineElements[0], new ArrayList<String>(List.of(rhsElements)));
    }

    public boolean isCFG() {
        if(!this.nonterminals.contains(this.startingSymbol))
            return false;
        for(String key: this.productions.keySet()) {
            if(!this.nonterminals.contains(key))
                return false;

            for(String element: this.productions.get(key)) {
                for(int i = 0; i < element.length(); i++) {
                    String charAsString = Character.toString(element.charAt(i));
                    if (!this.nonterminals.contains(charAsString) &&
                            !(this.terminals.contains(charAsString)) &&
                            !(charAsString.equals("E")))
                        return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "N=" + nonterminals +
                ", E=" + terminals +
                ", S='" + startingSymbol + '\'' +
                ", P=" + productions +
                '}';
    }

    public List<String> getNonterminals() {
        return nonterminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public Map<String, List<String>> getProductions() {
        return productions;
    }

    public List<String> getProductionsForNonterminal(String nonterminal) {
        return productions.get(nonterminal);
    }

    public String getStartingSymbol() { return startingSymbol; }
}
