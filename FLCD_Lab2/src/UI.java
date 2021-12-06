import utils.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UI {
    private FiniteAutomata finiteAutomata;

    public UI(FiniteAutomata finiteAutomata) {
        this.finiteAutomata = finiteAutomata;
    }

    private void displayMenu() {
        String menu = "\n";
        menu += "0. Exit.\n";
        menu += "1. Display the set of states.\n";
        menu += "2. Display the alphabet.\n";
        menu += "3. Display the transitions.\n";
        menu += "4. Display the set of final states.\n";
        menu += "5. Display FA details.\n";
        menu += "6. Verify if a given sequence is accepted by the FA.\n";

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

    private String getStatesAsString() {
        String result = "States = { ";
        for(String state: this.finiteAutomata.getStates())
            result += state + " ";
        result += "}";

        return result;
    }

    private String getAlphabetAsString() {
        String result = "Alphabet = { ";
        for(String element: this.finiteAutomata.getAlphabet())
            result += element + " ";
        result += "}";

        return result;
    }

    private String getTransitionsAsString() {
        String result = "Transitions = { \n";
        for(Pair element: this.finiteAutomata.getTransitions().keySet()) {
            String pair = "(" + element.getFirst() + ", " + element.getSecond() + ") -> ";
            for(String transitionResult: this.finiteAutomata.getTransitions().get(element))
                result += pair + transitionResult +"\n";
        }
        result += "}";

        return result;
    }

    private String getFinalStatesAsString() {
        String result = "Final states = { ";
        for(String element: this.finiteAutomata.getFinalStates())
            result += element + " ";
        result += "}";

        return result;
    }

    public String getFAAsString() {
        String result = getStatesAsString() + "\n" +
                getAlphabetAsString() + "\n" +
                "Starting symbol = " + this.finiteAutomata.getStartingSymbol() + "\n" +
                getFinalStatesAsString() + "\n" +
                getTransitionsAsString() + "\n";

        return result;
    }

    private void verifyAcceptedSequence() {
        String sequence = readStringFromConsole();
        if(this.finiteAutomata.verifyAcceptedSequence(sequence))
            System.out.println("The sequence is accepted by the FA.");
        else
            System.out.println("The sequence is NOT accepted by the FA.");
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
                case "1" -> System.out.println(getStatesAsString());
                case "2" -> System.out.println(getAlphabetAsString());
                case "3" -> System.out.println(getTransitionsAsString());
                case "4" -> System.out.println(getFinalStatesAsString());
                case "5" -> System.out.println(getFAAsString());
                case "6" -> verifyAcceptedSequence();
            }
        }
    }
}
