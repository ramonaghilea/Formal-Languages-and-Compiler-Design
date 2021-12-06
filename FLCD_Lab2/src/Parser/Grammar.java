package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    private List<String> nonterminals;
    private List<String> terminals;
    private String startingSymbol;
    private Map<List<String>, List<List<String>>> productions;

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
        String[] lhsElements = lineElements[0].split(" ");
        List<String> lhsElementsAsList = new ArrayList<>(List.of(lhsElements));

        for (String rhsElement : rhsElements) {
            if(this.productions.containsKey(lhsElementsAsList)) {
                String[] elementsInRhsElements = rhsElement.split(" ");
                List<String> elementsInRhsElementAsList = new ArrayList<>(List.of(elementsInRhsElements));
                this.productions.get(lhsElementsAsList).add(elementsInRhsElementAsList);
            }
            else {
                String[] elementsInRhsElements = rhsElement.split(" ");
                List<String> elementsInRhsElementAsList = new ArrayList<>(List.of(elementsInRhsElements));
                List<List<String>> rhsElementsAsList = new ArrayList<>();
                rhsElementsAsList.add(elementsInRhsElementAsList);
                this.productions.put(lhsElementsAsList, rhsElementsAsList);
            }
        }
    }

    public boolean isCFG() {
        if(!this.nonterminals.contains(this.startingSymbol))
            return false;

        for(List<String> key: this.productions.keySet()) {
            if(key.size() > 1)
                return false;

            if(!this.nonterminals.contains(key.get(0)))
                return false;

            for(List<String> rhsElement: this.productions.get(key)) {
                for(String elementInRhsElement: rhsElement) {
                    if (!this.nonterminals.contains(elementInRhsElement) &&
                            !(this.terminals.contains(elementInRhsElement)) &&
                            !(elementInRhsElement.equals("E"))) {
                        return false;
                    }
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

    public Map<List<String>, List<List<String>>> getProductions() {
        return productions;
    }

    public List<List<String>> getProductionsForNonterminal(String nonterminal) {
        return productions.get(new ArrayList<>(List.of(nonterminal)));
    }

    public boolean isNonterminal(String nonterminal) {
        return nonterminals.contains(nonterminal);
    }

    public String getStartingSymbol() { return startingSymbol; }
}
