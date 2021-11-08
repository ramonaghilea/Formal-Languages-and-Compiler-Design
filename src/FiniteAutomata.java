import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomata {
    private List<String> states;
    private List<String> alphabet;
    private String startingSymbol;
    private List<String> finalStates;
    private Map<Pair<String, String>, List<String>> transitions;

    private String fileName;

    public FiniteAutomata(String fileName) {
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.finalStates = new ArrayList<>();
        this.transitions = new HashMap<>();
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

        states = readLine(scanner.nextLine());
        alphabet = readLine(scanner.nextLine());
        startingSymbol = readLine(scanner.nextLine()).get(0);
        finalStates = readLine(scanner.nextLine());

        // read the transitions
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            readTransition(line);
        }
    }

    private List<String> readLine(String line) {
        String[] lineElements = line.split(" ");
        return Arrays.asList(lineElements);
    }

    private void readTransition(String line) {
        String[] lineElements = line.split(" ");
        if(isKeyInTransitions(new Pair<>(lineElements[0], lineElements[1])))
            getObjectFromTransitions(new Pair<>(lineElements[0], lineElements[1])).add(lineElements[2]);
        else
            this.transitions.put(new Pair<>(lineElements[0], lineElements[1]), new ArrayList<>(
                    Collections.singleton(lineElements[2])));
    }

    private boolean isKeyInTransitions(Pair<String, String> key) {
        for(Pair<String, String> i: this.transitions.keySet()) {
            if(i.getFirst().equals(key.getFirst()) && i.getSecond().equals(key.getSecond()))
                return true;
        }
        return false;
    }

    private List<String> getObjectFromTransitions(Pair<String, String> key) {
        for(Pair<String, String> i: this.transitions.keySet()) {
            if(i.getFirst().equals(key.getFirst()) && i.getSecond().equals(key.getSecond()))
                return this.transitions.get(i);
        }
        return List.of();
    }

    private boolean isDFA() {
        for(Pair<String, String> key: this.transitions.keySet()) {
            if(getObjectFromTransitions(key).size() > 1)
                return false;
        }
        return true;
    }

    public boolean verifyAcceptedSequence(String sequence) {
        // The Finite Automata is not a deterministic one.
        if(!isDFA())
            return false;

        String currentSymbol = this.startingSymbol;
        for(int i = 0; i < sequence.length(); i++) {
            if(isKeyInTransitions(new Pair<>(currentSymbol, Character.toString(sequence.charAt(i)))))
                currentSymbol = getObjectFromTransitions(new Pair<>(currentSymbol, Character.toString(sequence.charAt(i)))).get(0);
            else
                return false;
        }

        // The symbol is not a final symbol.
        if(!this.finalStates.contains(currentSymbol))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "FiniteAutomata{" +
                "Q=" + states +
                ", E=" + alphabet +
                ", q0='" + startingSymbol + '\'' +
                ", F=" + finalStates +
                ", d=" + transitions +
                '}';
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public Map<Pair<String, String>, List<String>> getTransitions() {
        return transitions;
    }

    public String getStartingSymbol() { return startingSymbol; }
}
