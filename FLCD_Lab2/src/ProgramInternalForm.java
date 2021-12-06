import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {
    private final List<Pair<String, Integer>> pairs;

    public ProgramInternalForm() {
        this.pairs = new ArrayList<>();
    }

    public void add(String token, Integer positionInSymbolTable) {
        this.pairs.add(new Pair<>(token, positionInSymbolTable));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Token  Index\n");
        for(Pair<String, Integer> pair: pairs) {
            result.append(pair.toString()).append("\n");
        }

        return result.toString();
    }
}
