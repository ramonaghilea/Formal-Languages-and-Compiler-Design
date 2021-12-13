package Parser;

import Errors.ParsingTableConflictError;
import utils.Pair;

import java.util.*;

public class LL1Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;
    private Map<String, Map<String, Pair<List<String>, Integer>>> table;

    private Map<Integer, Pair<List<String>, List<String>>> numberToProduction;

    public LL1Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        this.numberToProduction = new HashMap<>();

        initializeParsingTable();

        computeFirst();

        System.out.println("FIRST");
        for(String key: first.keySet()) {
            if(grammar.isNonterminal(key)) {
                System.out.println(key + " " + first.get(key));
            }
        }
        System.out.println("\n");

        computeFollow();

        System.out.println("FOLLOW");
        for(String key: follow.keySet()) {
            System.out.println(key + " " + follow.get(key));
        }
        System.out.println("\n");

        constructParsingTable();

        System.out.println(table);
    }

    private void initializeParsingTable() {
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
        this.table.get("$").put("$",
                new Pair<>(new ArrayList<>(Collections.singleton("acc")), 0));

        for(String terminal: terminals) {
            this.table.put(terminal, new HashMap<>());
            for(String terminal2: terminals) {
                if(terminal.equals(terminal2))
                    this.table.get(terminal).put(terminal2,
                            new Pair<>(new ArrayList<>(Collections.singleton("pop")), 0));
                else
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
                }
            }

            // check if there were any modifications in the first set
            // if there were, continue the loop, otherwise end the algorithm
            for(String nonterminal: nonterminals) {
                if (!first.get(nonterminal).equals(previousFirst.get(nonterminal))) {
                    isFirstSetChanged = true;
                    // update previousFirst
                    previousFirst.get(nonterminal).addAll(first.get(nonterminal));
                }
            }
        }
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

                for (List<String> key : productions.keySet()) {
                    for (List<String> rhsElement : productions.get(key)) {
                        for (int index = 0; index < rhsElement.size(); index++) {

                            String currentNonterminalForFollow = rhsElement.get(index);

                            if(!currentNonterminalForFollow.equals(nonterminal))
                                continue;

                            // get the firstSet for the following symbol
                            Set<String> firstSetForFollowingSymbol = new HashSet<>();
                            if (index < rhsElement.size() - 1)
                                firstSetForFollowingSymbol = first.get(rhsElement.get(index + 1));
                            else
                                this.follow.get(nonterminal).addAll(previousFollow.get(key.get(0)));

                            for (String elementInFirstSet : firstSetForFollowingSymbol) {
                                if (elementInFirstSet.equals("E"))
                                    this.follow.get(nonterminal).addAll(previousFollow.get(key.get(0)));
                                else
                                    this.follow.get(nonterminal).addAll(first.get(rhsElement.get(index + 1)));
                            }
                        }
                    }
                }
            }

            for(String nonterminal: nonterminals) {
                if (!follow.get(nonterminal).equals(previousFollow.get(nonterminal))) {
                    isFollowSetChanged = true;
                    // update previousFollow
                    previousFollow.get(nonterminal).addAll(follow.get(nonterminal));
                }
            }
        }
    }

    private void constructParsingTable() {
        Map<List<String>, List<List<String>>> productions = this.grammar.getProductions();

        int indexOfProduction = 0;
        for(List<String> key: productions.keySet()) {
            for(List<String> rhsElement: productions.get(key)) {
                indexOfProduction++;
                numberToProduction.put(indexOfProduction, new Pair<>(new ArrayList<>(key),
                        new ArrayList<>(rhsElement)));

                // compute first for the rhs of every production
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


                // populate the table
                for(String element: firstConcatenationResult) {
                    if(!element.equals("E"))
                        // table(key, terminal) = (rhs, indexOfProduction)
                        if(this.table.get(key.get(0)).get(element) == null)
                            this.table.get(key.get(0)).put(element,
                                    new Pair<>(rhsElement, indexOfProduction));
                        else
                            throw new ParsingTableConflictError(key.get(0), element,
                                    this.table.get(key.get(0)).get(element).toString(),
                                    new Pair<>(rhsElement, indexOfProduction).toString());
                    else
                    {
                        for(String elementInFollowKey: this.follow.get(key.get(0))) {
                            if(!elementInFollowKey.equals("E")) {
                                if(this.table.get(key.get(0)).get(elementInFollowKey) == null)
                                    this.table.get(key.get(0)).put(elementInFollowKey,
                                            new Pair<>(rhsElement, indexOfProduction));
                                else
                                    throw new ParsingTableConflictError(key.get(0), elementInFollowKey,
                                            this.table.get(key.get(0)).get(elementInFollowKey).toString(),
                                            new Pair<>(rhsElement, indexOfProduction).toString());
                            }
                            else
                                if(this.table.get(key.get(0)).get("$") == null)
                                    this.table.get(key.get(0)).put("$",
                                            new Pair<>(rhsElement, indexOfProduction));
                                else
                                    throw new ParsingTableConflictError(key.get(0), "$",
                                            this.table.get(key.get(0)).get("$").toString(),
                                            new Pair<>(rhsElement, indexOfProduction).toString());
                        }
                    }
                }
            }
        }
    }

    public List<Integer> analyseSequence(String sequence) {
        String[] sequenceElements = sequence.split(" ");
        List<String> sequenceElementsAsList = Arrays.asList(sequenceElements);

        Stack<String> inputStack = new Stack<>();
        Stack<String> workingStack = new Stack<>();
        List<Integer> output = new ArrayList<>();

        inputStack.push("$");
        for (int i = sequenceElementsAsList.size() - 1; i >= 0; i--)
            inputStack.push(sequenceElementsAsList.get(i));

        workingStack.push("$");
        workingStack.push(grammar.getStartingSymbol());

        boolean parsing = true;
        while (parsing) {
            System.out.println(inputStack);
            System.out.println(workingStack);
            System.out.println(output);
            System.out.println("\n");

            if(workingStack.peek().equals("E"))
                workingStack.pop();

            if (table.get(workingStack.peek()).get(inputStack.peek()) == null) {
                // error
                parsing = false;
                System.out.println("Sequence not accepted.");
                System.out.println("Error for symbol " + workingStack.peek());
            } else {
                Pair<List<String>, Integer> elementInTable =
                        table.get(workingStack.peek()).get(inputStack.peek());
                if (elementInTable.getSecond() != 0) {
                    // push

                    // pop the lhs of the production from the working stack and push the rhs
                    workingStack.pop();
                    for (int i = elementInTable.getFirst().size() - 1; i >= 0; i--)
                        workingStack.push(elementInTable.getFirst().get(i));

                    // add the index of the production to the output
                    output.add(elementInTable.getSecond());
                } else if (elementInTable.getFirst().get(0).equals("pop")) {
                    // pop

                    // pop from the input stack and the working stack
                    inputStack.pop();
                    workingStack.pop();
                } else {
                    // acc
                    parsing = false;
                    System.out.println("Sequence accepted.");
                    System.out.println(output);

                    for(Integer index : output) {
                        System.out.println(numberToProduction.get(index).getFirst());
                        System.out.println(numberToProduction.get(index).getSecond());
                    }
                }
            }
        }

        return output;
    }

    public Map<Integer, Pair<List<String>, List<String>>> getNumberToProduction() {
        return numberToProduction;
    }
}
