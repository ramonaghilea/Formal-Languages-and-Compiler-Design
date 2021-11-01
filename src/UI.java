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
        menu += "0. Exit\n";
        menu += "1. Display the set of states\n";
        menu += "2. Display the alphabet\n";
        menu += "3. Display the transitions\n";
        menu += "4. Display the set of final states\n";

        System.out.println(menu);
    }

    public void run() {
        Boolean running = true;
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
                case "1" -> System.out.println(this.finiteAutomata.getQ());
                case "2" -> System.out.println(this.finiteAutomata.getE());
                case "3" -> System.out.println(this.finiteAutomata.getD());
                case "4" -> System.out.println(this.finiteAutomata.getF());
            }
        }
    }
}
