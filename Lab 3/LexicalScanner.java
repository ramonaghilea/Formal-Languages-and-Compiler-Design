import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LexicalScanner {
    private List<String> elements;
    private List<String> tokens;
    private String tokensFileName = "src/files/token.in";

    public LexicalScanner() {
        this.elements = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.parseTokensFile();
    }

    public void parseFile(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                parseLine(line);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //System.out.println(line);
        }
        scanner.close();

        for(String string: elements)
            System.out.println(string);
    }

    public void parseTokensFile() {
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
            //System.out.println(line);
        }
        scanner.close();
    }

    public void parseLine(String line) throws FileNotFoundException {
        // remove all blank spaces
        String[] lineElements = line.split("\\s+");

        for(String element: lineElements) {
            if(element.length() > 0) {
                int i = 0;
                StringBuilder newString = new StringBuilder();

                while(i < element.length()) {
                    if(isCharacterInAlphabet(element.charAt(i)))
                        newString.append(element.charAt(i));
                    else {
                        if(newString.length() > 0)
                            this.elements.add(newString.toString());
                        newString = new StringBuilder();

                        String newToken = Character.toString(element.charAt(i));
                        if(this.tokens.contains(newToken))
                            this.elements.add(Character.toString(element.charAt(i)));
                    }
                    i++;
                }
                if(newString.length() > 0)
                    this.elements.add(newString.toString());
            }
        }
    }

    private boolean isCharacterInAlphabet(Character character) {
        if(character >= 'a' && character <= 'z')
            return true;
        if(character >= 'A' && character <= 'Z')
            return true;
        if(character >= '0' && character <= '9')
            return true;
        if(character.equals('_'))
            return true;
        return false;
    }
}
