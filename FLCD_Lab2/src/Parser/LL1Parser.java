package Parser;

import utils.Pair;

import java.util.*;

public class LL1Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;
    private Map<String, Map<String, Pair<List<String>, Integer>>> table;

    public LL1Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();

        initializeTable();

        computeFirst();
        computeFollow();
        constructTable();

        System.out.println(table);
    }

    private void initializeTable() {
        this.table = new HashMap<>();
        List<String> nonterminals = this.grammar.getNonterminals();
        List<String> terminals = this.grammar.getTerminals();

        for(String nonterminal: nonterminals) {
            this.table.put(nonterminal, new HashMap<>());
            for(String terminal: terminals) {
                this.table.get(nonterminal).put(terminal, null);
            }
        }
        this.table.put("$", new HashMap<>());
        for(String terminal: terminals) {
            this.table.get("$").put(terminal, null);
        }
        this.table.get("$").put("$", new Pair<>(new ArrayList<>(Collections.singleton("acc")), 0));

        for(String terminal: terminals) {
            this.table.put(terminal, new HashMap<>());
            for(String terminal2: terminals) {
                if(terminal.equals(terminal2))
                    this.table.get(terminal).put(terminal2, new Pair<>(new ArrayList<>(Collections.singleton("pop")), 0));
                this.table.get(terminal).put(terminal2, null);
            }
        }
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

//    private void computeFirst() {
//        List<String> nonterminals = this.grammar.getNonterminals();
//        for(String nonterminal: nonterminals) {
//            this.first.put(nonterminal, new HashSet<>());
//        }
//
//        List<String> terminals = this.grammar.getTerminals();
//        for(String terminal: terminals) {
//            this.first.put(terminal, new HashSet<>());
//        }
//
//        Map<List<String>, List<List<String>>> productions = this.grammar.getProductions();
//
//        // initialization
//        for(String nonterminal: nonterminals) {
//            List<List<String>> productionsForNonterminal = this.grammar.getProductionsForNonterminal(nonterminal);
//            for(List<String> rhsElement: productionsForNonterminal) {
//                // check if the first element in the list is a terminal and if it is, add it to the firstSet
//                // if it is E, add it to the firstSet
//                if(terminals.contains(rhsElement.get(0)) || rhsElement.get(0).equals("E"))
//                    this.first.get(nonterminal).add(rhsElement.get(0));
//            }
//        }
//        for(String terminal: terminals) {
//            // first for a terminal is itself
//            this.first.get(terminal).add(terminal);
//        }
//
//        // loop
//        boolean isFirstSetChanged = true;
//        while(isFirstSetChanged) {
//            isFirstSetChanged = false;
//
//            for(List<String> key: productions.keySet()) {
//                for(List<String> rhsElement: productions.get(key)) {
//                    Set<String> firstCopy = first.get(key.get(0));
//                    Set<String> concatenationResult = new HashSet<>();
//
//                    if(rhsElement.get(0).equals("E"))
//                    {
//                        continue;
//                    }
//                    else if(rhsElement.size() == 1)
//                    {
//                        if(terminals.contains(rhsElement.get(0)))
//                            concatenationResult.add(rhsElement.get(0));
//                        else
//                            concatenationResult.addAll(this.first.get(rhsElement.get(0)));
//                    }
//                    else {
//                        // compute the concatenation of len 1 for the previous first sets of the elements in the current rhsElement
//                        concatenationResult = concatenationOfLenOneForSets(this.first.get(rhsElement.get(0)),
//                                this.first.get(rhsElement.get(1)));
//                        for (int i = 2; i < rhsElement.size(); i++) {
//                            concatenationResult = concatenationOfLenOneForSets(concatenationResult,
//                                    this.first.get(rhsElement.get(i)));
//                        }
//                    }
//
//                    firstCopy.addAll(concatenationResult);
//
//                    if(!firstCopy.equals(first.get(key.get(0)))) {
//                        isFirstSetChanged = true;
//                        // update first
//                        this.first.get(key.get(0)).addAll(firstCopy);
//                    }
//
//                }
//            }
//        }
//
//        System.out.println("FIRST");
//        for(String key: first.keySet()) {
//            if(grammar.isNonterminal(key)) {
//                System.out.println(key + " " + first.get(key));
//            }
//        }
//        System.out.println("\n");
//    }

    private void computeFirst() {
        List<String> nonterminals = this.grammar.getNonterminals();
        Map<String, Set<String>> previousFirst = new HashMap<>();
        for(String nonterminal: nonterminals) {
            this.first.put(nonterminal, new HashSet<>());
            previousFirst.put(nonterminal, new HashSet<>());
        }

        List<String> terminals = this.grammar.getTerminals();
        for(String terminal: terminals) {
            this.first.put(terminal, new HashSet<>());
            previousFirst.put(terminal, new HashSet<>());
        }

        Map<List<String>, List<List<String>>> productions = this.grammar.getProductions();

        // initialization
        for(String nonterminal: nonterminals) {
            List<List<String>> productionsForNonterminal = this.grammar.getProductionsForNonterminal(nonterminal);
            for(List<String> rhsElement: productionsForNonterminal) {
                // check if the first element in the list is a terminal and if it is, add it to the firstSet
                // if it is E, add it to the firstSet
                if(terminals.contains(rhsElement.get(0)) || rhsElement.get(0).equals("E")) {
                    this.first.get(nonterminal).add(rhsElement.get(0));
                    previousFirst.get(nonterminal).add(rhsElement.get(0));
                }
            }
        }
        for(String terminal: terminals) {
            // first for a terminal is itself
            this.first.get(terminal).add(terminal);
            previousFirst.get(terminal).add(terminal);
        }

        // loop
        boolean isFirstSetChanged = true;
        while(isFirstSetChanged) {
            isFirstSetChanged = false;

            for(List<String> key: productions.keySet()) {
                for(List<String> rhsElement: productions.get(key)) {
                    //Set<String> firstCopy = first.get(key.get(0));
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
                            concatenationResult.addAll(previousFirst.get(rhsElement.get(0)));
                    }
                    else {
                        // compute the concatenation of len 1 for the previous first sets of the elements in the current rhsElement
                        concatenationResult = concatenationOfLenOneForSets(previousFirst.get(rhsElement.get(0)),
                                previousFirst.get(rhsElement.get(1)));
                        for (int i = 2; i < rhsElement.size(); i++) {
                            concatenationResult = concatenationOfLenOneForSets(concatenationResult,
                                    previousFirst.get(rhsElement.get(i)));
                        }
                    }

                    this.first.get(key.get(0)).addAll(concatenationResult);

//                    if(!firstCopy.equals(first.get(key.get(0)))) {
//                        isFirstSetChanged = true;
//                        // update first
//                        this.first.get(key.get(0)).addAll(firstCopy);
//                    }

                }
            }

            for(String nonterminal: nonterminals) {
                if (!first.get(nonterminal).equals(previousFirst.get(nonterminal))) {
                    isFirstSetChanged = true;
                    // update previousFollow
                    previousFirst.get(nonterminal).addAll(first.get(nonterminal));
                }
            }
        }

        System.out.println("FIRST");
        for(String key: first.keySet()) {
            if(grammar.isNonterminal(key)) {
                System.out.println(key + " " + first.get(key));
            }
        }
        System.out.println("\n");
    }

    private void computeFollow() {
        // initialization
        List<String> nonterminals = this.grammar.getNonterminals();
        Map<String, Set<String>> previousFollow = new HashMap<>();
        for(String nonterminal: nonterminals) {
            this.follow.put(nonterminal, new HashSet<>());
            previousFollow.put(nonterminal, new HashSet<>());
        }
        // for the starting symbol add epsilon
        this.follow.get(grammar.getStartingSymbol()).add("E");

        Map<List<String>, List<List<String>>> productions = this.grammar.getProductions();

        // loop
        boolean isFollowSetChanged = true;
        while(isFollowSetChanged) {
            isFollowSetChanged = false;

            for(String nonterminal: nonterminals) {
                // create a copy to compare it with the previous one
                //Set<String> followCopyForNonterminal = follow.get(nonterminal);

                for (List<String> key : productions.keySet()) {
                    for (List<String> rhsElement : productions.get(key)) {
                        for (int index = 0; index < rhsElement.size(); index++) {

                            String currentNonterminalForFollow = rhsElement.get(index);
                            //System.out.println("terminalfollow: " + currentNonterminalForFollow);

                            if(! currentNonterminalForFollow.equals(nonterminal))
                                continue;

//                            if (grammar.isNonterminal(currentNonterminalForFollow)) {

//                                // create a copy to compare it with the previous one
//                                Set<String> followCopyForNonterminal = follow.get(currentNonterminalForFollow);

                                // get the firstSet for the following symbol
                                Set<String> firstSetForFollowingSymbol = new HashSet<>();
                                if (index < rhsElement.size() - 1)
                                    firstSetForFollowingSymbol = first.get(rhsElement.get(index + 1));
                                else
                                    this.follow.get(nonterminal).addAll(previousFollow.get(key.get(0)));

                                for (String elementInFirstSet : firstSetForFollowingSymbol) {
                                    //System.out.println(key);
                                    if (elementInFirstSet.equals("E"))
                                        this.follow.get(nonterminal).addAll(previousFollow.get(key.get(0)));
                                    else
                                        this.follow.get(nonterminal).addAll(first.get(rhsElement.get(index + 1)));
                                }

//                                if (!followCopyForNonterminal.equals(follow.get(currentNonterminalForFollow))) {
//                                    isFollowSetChanged = true;
//                                    // update follow
//                                    this.follow.get(currentNonterminalForFollow).addAll(followCopyForNonterminal);
//                                }
//                            }
                        }
                    }
                }

//                if (!follow.get(nonterminal).equals(previousFollow.get(nonterminal))) {
//                    isFollowSetChanged = true;
//                    // update follow
//                    //this.follow.get(nonterminal).addAll(followCopyForNonterminal);
//                }
            }

            for(String nonterminal: nonterminals) {
                if (!follow.get(nonterminal).equals(previousFollow.get(nonterminal))) {
                    isFollowSetChanged = true;
                    // update previousFollow
                    previousFollow.get(nonterminal).addAll(follow.get(nonterminal));
                }
            }
        }

        System.out.println("FOLLOW");
        for(String key: follow.keySet()) {
            System.out.println(key + " " + follow.get(key));
        }
    }

    private void constructTable() {
        Map<List<String>, List<List<String>>> productions = this.grammar.getProductions();

        int indexOfProduction = 0;
        for(List<String> key: productions.keySet()) {
            for(List<String> rhsElement: productions.get(key)) {
                indexOfProduction++;

                Set<String> firstConcatenationResult = new HashSet<>();
                if(rhsElement.size() > 1) {
                    firstConcatenationResult =
                            concatenationOfLenOneForSets(this.first.get(rhsElement.get(0)),
                                    this.first.get(rhsElement.get(1)));
                    for (int i = 2; i < rhsElement.size(); i++) {
                        firstConcatenationResult = concatenationOfLenOneForSets(firstConcatenationResult,
                                this.first.get(rhsElement.get(i)));
                    }
                }
                else {
                    if(rhsElement.get(0).equals("E"))
                        firstConcatenationResult.add("E");
                    else
                        firstConcatenationResult = this.first.get(rhsElement.get(0));
                }
                //System.out.println(rhsElement);
                for(String element: firstConcatenationResult) {
                    if(!element.equals("E"))
                        this.table.get(key.get(0)).put(element, new Pair<>(rhsElement, indexOfProduction));
                    else
                    {
                        for(String elementInFollowKey: this.follow.get(key.get(0))) {
                            if(!elementInFollowKey.equals("E"))
                                this.table.get(key.get(0)).put(elementInFollowKey, new Pair<>(rhsElement, indexOfProduction));
                        }
                    }
                }
            }
        }
    }

    public void evaluateSequence(List<String> sequence) {
        
    }
}
