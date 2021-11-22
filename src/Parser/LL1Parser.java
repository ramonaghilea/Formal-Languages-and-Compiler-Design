package Parser;

import java.util.*;

public class LL1Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public LL1Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();

        List<String> nonterminals = this.grammar.getNonterminals();
        for(String nonterminal: nonterminals) {
            this.first.put(nonterminal, new HashSet<>());
            this.follow.put(nonterminal, new HashSet<>());
        }

        List<String> terminals = this.grammar.getTerminals();
        for(String terminal: terminals) {
            this.first.put(terminal, new HashSet<>());
        }

        computeFirst();
    }

    private String concatenationOfLenOne(String a, String b) {
        if(a.equals("E"))
            return b;
        return a;
    }

    private Set<String> concatenationOfLenOneForSets(Set<String> a, Set<String> b) {
        Set<String> newSet = new HashSet<>();
        if(a.isEmpty())
            return b;
        if(b.isEmpty())
            return a;
        for(String elemA: a) {
            for(String elemB: b)
                newSet.add(concatenationOfLenOne(elemA, elemB));
        }

        return newSet;
    }

    private void computeFirst() {
        List<String> nonterminals = this.grammar.getNonterminals();
        List<String> terminals = this.grammar.getTerminals();
        Map<List<String>, List<List<String>>> productions = this.grammar.getProductions();

        // initialization
        for(String nonterminal: nonterminals) {
            List<List<String>> productionsForNonterminal = this.grammar.getProductionsForNonterminal(nonterminal);
            for(List<String> rhsElement: productionsForNonterminal) {
                // check if the first element in the list is a terminal and if it is, add it to the firstSet
                // if it is E, add it to the firstSet
                if(terminals.contains(rhsElement.get(0)) || rhsElement.get(0).equals("E"))
                    this.first.get(nonterminal).add(rhsElement.get(0));
            }
        }
        for(String terminal: terminals) {
            // first for a terminal is itself
            this.first.get(terminal).add(terminal);
        }

        boolean isFirstSetChanged = true;
        while(isFirstSetChanged) {
            isFirstSetChanged = false;

            for(List<String> key: productions.keySet()) {
                for(List<String> rhsElement: productions.get(key)) {
                    Set<String> firstCopy = first.get(key.get(0));
                    Set<String> concatenationResult = new HashSet<>();

                    if(rhsElement.get(0).equals("E"))
                    {
                        continue;
                    }
                    else if(rhsElement.size() == 1)
                    {
                        if(terminals.contains(rhsElement.get(0)))
                            concatenationResult.add(rhsElement.get(0));
                        else
                            concatenationResult.addAll(this.first.get(rhsElement.get(0)));
                    }
                    else {
                        // compute the concatenation of len 1 for the previous first sets of the elements in the current rhsElement
                        concatenationResult = concatenationOfLenOneForSets(this.first.get(rhsElement.get(0)),
                                this.first.get(rhsElement.get(1)));
                        for (int i = 2; i < rhsElement.size(); i++) {
                            concatenationResult = concatenationOfLenOneForSets(concatenationResult,
                                    this.first.get(rhsElement.get(i)));
                        }
                    }

                    firstCopy.addAll(concatenationResult);

                    if(!firstCopy.equals(first.get(key.get(0)))) {
                        isFirstSetChanged = true;
                        // update first
                        this.first.get(key.get(0)).addAll(firstCopy);
                    }

                }
            }
        }

        for(String key: first.keySet()) {
            System.out.println(key + " " + first.get(key));
        }
    }

    private void computeFollow() {

    }
}
