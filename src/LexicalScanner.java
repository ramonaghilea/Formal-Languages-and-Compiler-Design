import Errors.LexicalError;

import java.io.*;
import java.util.*;

public class LexicalScanner {
    private final List<String> tokens;
    private final List<String> separators;
    private final List<String> operators;
    private final SymbolTable symbolTable;
    private final ProgramInternalForm programInternalForm;

    private final String tokensFileName = "src/files/lexicalScanner/token.in";
    private final String PIFOutput = "src/files/lexicalScanner/PIF.out";
    private final String STOutput = "src/files/lexicalScanner/ST.out";

    public LexicalScanner() {
        this.tokens = new ArrayList<>();
        this.separators = Arrays.asList(";", ":", "(", ")", "[", "]", "{", "}", " ");
        this.operators = Arrays.asList("+", "-", "*", "/", "%", "<", "<=", ">", ">=", "=", "!=", ":=");
        this.symbolTable = new SymbolTable();
        this.programInternalForm = new ProgramInternalForm();

        this.readTokensFile();
    }

    public void scanProgramFile(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int lineNumber = 1;
        StringBuilder errorMessage = new StringBuilder();
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    List<String> elementsOnLine = tokenizeLine(line);
                    scanElementsOnLine(elementsOnLine, lineNumber);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (LexicalError le) {
                    errorMessage.append(le.getMessage()).append("\n");
                }
                lineNumber++;
            }
            if(errorMessage.length() > 0)
                System.out.println(errorMessage);
            else
                System.out.println("Lexically correct");
        } finally {
            scanner.close();
        }

        writeResultsToFiles();
    }

    private void readTokensFile() {
        File file = new File(this.tokensFileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            this.tokens.add(line);
        }
        scanner.close();
    }

    private List<String> tokenizeLine(String line) throws FileNotFoundException {
        List<String> elementsOnLine = new ArrayList<>();
        int i = 0;

        while(i < line.length()) {
            if(line.charAt(i) == ' ') {
                i++;
            }
            else if(line.charAt(i) == '\'') {
                // possible char constant
                i++;
                StringBuilder newString = new StringBuilder();
                newString.append('\'');

                while (i < line.length() && line.charAt(i) != '\'') {
                    newString.append(line.charAt(i));
                    i++;
                }

                // do the check in case there is an unclosed character literal
                if(i < line.length()) {
                    newString.append('\'');
                    i++;
                }
                elementsOnLine.add(newString.toString());
            }
            else if(line.charAt(i) == '"') {
                // possible string constant
                i++;
                StringBuilder newString = new StringBuilder();
                newString.append('"');

                while (i < line.length() && line.charAt(i) != '"') {
                    newString.append(line.charAt(i));
                    i++;
                }

                // do the check in case there is an unclosed string literal
                if(i < line.length()) {
                    newString.append('"');
                    i++;
                }
                elementsOnLine.add(newString.toString());
            }
            else if(this.tokens.contains(Character.toString(line.charAt(i))) || line.charAt(i) =='!') {
                // check for compound operators
                // the second check is made for this case which is not a compound operator: ");"
                if(i < line.length() - 1 &&
                        this.tokens.contains(String.valueOf(line.charAt(i)) + line.charAt(i + 1))) {
                    elementsOnLine.add(String.valueOf(line.charAt(i)) + line.charAt(i + 1));

                    i += 2;
                }
                else {
                    elementsOnLine.add(Character.toString((line.charAt(i))));
                    i++;
                }
            }
            else {
                // reserved words, AND OR operators, identifiers, constants
                StringBuilder newString = new StringBuilder();
                while(i < line.length() && !this.separators.contains(Character.toString(line.charAt(i)))) {
                    newString.append(line.charAt(i));
                    i++;
                }

                // the new string could contain both operators and constants/identifiers
                int j = 0;
                while (j < newString.length()) {
                    StringBuilder newSubstring = new StringBuilder();
                    while (j < newString.length() && !this.operators.contains(Character.toString(newString.charAt(j)))) {
                        newSubstring.append(newString.charAt(j));
                        j++;
                    }
                    elementsOnLine.add(newSubstring.toString());

                    // check for compound operators
                    if(j < newString.length() - 1 &&
                            this.tokens.contains(String.valueOf(newString.charAt(j)) + newString.charAt(j + 1))) {
                        elementsOnLine.add(String.valueOf(newString.charAt(j)) + newString.charAt(j + 1));

                        j += 2;
                    }
                    else if(j < newString.length()){
                        elementsOnLine.add(Character.toString(newString.charAt(j)));
                        j++;
                    }
                }
            }
        }

        return elementsOnLine;
    }

    private void scanElementsOnLine(List<String> elements, int lineNumber) {
        String previousToken = ""; //used to deciding if +/- is binary/unary
        for(int i = 0; i < elements.size(); i++) {
            if(this.tokens.contains(elements.get(i))) {
                // token

                // special cases for binary/unary "-" and "-0"
                if (elements.get(i).equals("-")) {
                    if(previousToken.length() > 0 && isNumericalConstant(previousToken)) {
                        // "-" is a binary operator
                        this.programInternalForm.add(elements.get(i), 0);
                    }
                    else {
                        // "-" is a unary operator
                        if (i < elements.size() - 1 && elements.get(i + 1).equals("0"))
                            throw new LexicalError(lineNumber, "Cannot have constant -0");

                        else if (i < elements.size() - 1 && isNumericalConstant(elements.get(i + 1))) {
                            String negativeNumericalConstant = "-" + elements.get(i + 1);
                            if (this.symbolTable.search(negativeNumericalConstant) == -1)
                                this.symbolTable.add(negativeNumericalConstant);
                            int positionInSymbolTable = this.symbolTable.search(negativeNumericalConstant);
                            this.programInternalForm.add("ct", positionInSymbolTable);

                            i++; // increase i because it analysed the next token as well
                        }
                    }
                }
                else
                    this.programInternalForm.add(elements.get(i), 0);
            }
            else if(isIdentifier(elements.get(i))){
                if(this.symbolTable.search(elements.get(i)) == -1)
                    this.symbolTable.add(elements.get(i));
                int positionInSymbolTable = this.symbolTable.search(elements.get(i));
                this.programInternalForm.add("id", positionInSymbolTable);
            }
            else if(isConstant(elements.get(i))) {
                if(this.symbolTable.search(elements.get(i)) == -1)
                    this.symbolTable.add(elements.get(i));
                int positionInSymbolTable = this.symbolTable.search(elements.get(i));
                this.programInternalForm.add("ct", positionInSymbolTable);
            }
            // special cases for identifiers
            else if(isLowercaseCharacter(Character.toString(elements.get(i).charAt(0))) && (elements.get(i).length() == 1 ||
                    isIdentifier(elements.get(i).substring(1))))
                throw new LexicalError(lineNumber, "Identifier starts with lowercase letter " + elements.get(i));
            else if(isNumericalConstant(Character.toString(elements.get(i).charAt(0))) &&
                    isIdentifier(elements.get(i).substring(1)))
                throw new LexicalError(lineNumber, "Identifier starts with digit " + elements.get(i));
            else
                throw new LexicalError(lineNumber, "Invalid token " + elements.get(i));

            previousToken = elements.get(i);
        }
    }

    private boolean isIdentifier(String element){
        String identifier = "^[A-Z_]([a-zA-Z0-9_]*)$";
        return element.matches(identifier);
    }

    private boolean isConstant(String element){
        return isCharacterConstant(element) ||
                isStringConstant(element) ||
                isNumericalConstant(element) ||
                isBooleanConstant(element);
    }

    private boolean isCharacterConstant(String element){
        String characterConstant = "^\'[a-zA-Z0-9 ]\'$";
        return element.matches(characterConstant);
    }

    private boolean isStringConstant(String element){
        String stringConstant = "^\"[a-zA-Z0-9 ]*\"$";
        return element.matches(stringConstant);
    }

    private boolean isNumericalConstant(String element){
        String numberConstant = "^([1-9][0-9]*)|0$";
        return element.matches(numberConstant);
    }

    private boolean isBooleanConstant(String element){
        return element.equals("true") || element.equals("false");
    }

    private boolean isLowercaseCharacter(String element){
        String characterConstant = "^[a-z]$";
        return element.matches(characterConstant);
    }

    private void writeToFile(String fileName, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();
    }

    private void writeResultsToFiles() {
        try {
            writeToFile(this.PIFOutput, this.programInternalForm.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeToFile(this.STOutput, this.symbolTable.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

