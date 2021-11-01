import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteAutomata {
    private List<String> Q;
    private List<String> E;
    private String q0;
    private List<String> F;
    private Map<Pair<String, String>, String> d;

    public FiniteAutomata() {
        this.Q = new ArrayList<>();
        this.E = new ArrayList<>();
        this.F = new ArrayList<>();
        this.d = new HashMap<>();
    }

    public void readFile(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Q = readLine(scanner.nextLine());
        E = readLine(scanner.nextLine());
        q0 = readLine(scanner.nextLine()).get(0);
        F = readLine(scanner.nextLine());

        // read the line with "d ="
        scanner.nextLine();

        // read the transitions
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            readTransition(line);
        }
    }

    public List<String> readLine(String line) {
        String[] lineElements = line.split(" ");
        List<String> lineElementsAsList = Arrays.asList(lineElements);
        return lineElementsAsList.subList(2, lineElementsAsList.size());
    }

    public void readTransition(String line) {
        String[] lineElements = line.split("->");
        String[] pair = lineElements[0].split(",");
        this.d.put(new Pair<>(pair[0].trim(), pair[1].trim()), lineElements[1].trim());
    }

    @Override
    public String toString() {
        return "FiniteAutomata{" +
                "Q=" + Q +
                ", E=" + E +
                ", q0='" + q0 + '\'' +
                ", F=" + F +
                ", d=" + d +
                '}';
    }

    public List<String> getQ() {
        return Q;
    }

    public List<String> getE() {
        return E;
    }

    public List<String> getF() {
        return F;
    }

    public Map<Pair<String, String>, String> getD() {
        return d;
    }
}
