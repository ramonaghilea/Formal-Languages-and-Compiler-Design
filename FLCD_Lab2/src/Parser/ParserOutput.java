package Parser;

import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ParserOutput {
    private Grammar grammar;
    private List<Integer> productionsAsNumbersList;
    private Map<Integer, Pair<List<String>, List<String>>> numberToProduction;

    private List<Node> nodeList;

    public ParserOutput(List<Integer> productionsAsNumbersList,
                        Map<Integer, Pair<List<String>, List<String>>> numberToProduction,
                        Grammar grammar) {
        this.productionsAsNumbersList = productionsAsNumbersList;
        this.numberToProduction = numberToProduction;
        this.grammar = grammar;
        this.nodeList = new ArrayList<>();

        transformOutputIntoTree();
        System.out.println("\n");
        //System.out.println(nodeList);
    }

    private void transformOutputIntoTree() {
        int currentNodeIndex = 0;
        int currentProductionIndex = 0;
        Stack<Node> nodeStack = new Stack<>();

        Node root = new Node();
        root.setFatherIndex(0);
        currentNodeIndex++;
        root.setIndex(currentNodeIndex);
        root.setValue(numberToProduction.get(
                productionsAsNumbersList.get(currentProductionIndex)).getFirst().get(0));
        nodeList.add(root);
        nodeStack.push(root);

        while(currentProductionIndex < productionsAsNumbersList.size() && !nodeStack.isEmpty()) {
            System.out.println(nodeStack);

            Node currentNode = nodeStack.pop();

            if(this.grammar.getTerminals().contains(currentNode.getValue()) ||
                    currentNode.getValue().equals("E"))
                continue;

            List<String> children = numberToProduction.get(
                    productionsAsNumbersList.get(currentProductionIndex)).getSecond();
            currentNodeIndex += children.size();
            for (int i = children.size() - 1; i >= 0; i--) {
                Node newNode = new Node();
                newNode.setFatherIndex(currentNode.getIndex());
                newNode.setIndex(currentNodeIndex);
                currentNodeIndex--;
                newNode.setValue(children.get(i));
                nodeList.add(newNode);
                nodeStack.push(newNode);

                if (i != 0)
                    newNode.setRightSiblingIndex(currentNodeIndex);
            }

            currentNodeIndex += children.size();
            currentProductionIndex++;
        }

        nodeList.sort(Comparator.comparing(Node::getIndex));
    }

    private String getTreeAsString() {
        StringBuilder result = new StringBuilder("Index | Info | Parent | Right sibling\n");
        for(Node node : nodeList)
            result.append(node.getIndex()).append(" | ")
                    .append(node.getValue()).append(" | ")
                    .append(node.getFatherIndex()).append(" | ")
                    .append(node.getRightSiblingIndex()).append("\n");

        return String.valueOf(result);
    }

    public void printToConsole() {
        String result = getTreeAsString();
        System.out.println(result);
    }

    public void printToFile(String fileName) {
        String result = getTreeAsString();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
