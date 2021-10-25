import java.util.Objects;

public class SymbolTable {
    private final String[] elements;
    private final int capacity;

    public SymbolTable() {
        this.capacity = 31;
        this.elements = new String[capacity];
    }

    public boolean add(String key) {
        if(this.search(key) != -1)
            return false;

        int i = 0;
        int position = this.hashFunction(key, i);

        while(this.elements[position] != null && i < this.capacity - 1) {
            i++;
            position = this.hashFunction(key, i);
        }

        this.elements[position] = key;
        return true;
    }

    public int search(String key) {
        int i = 0;
        int position = this.hashFunction(key, i);

        while(!Objects.equals(this.elements[position], key) && i < this.capacity - 1) {
            i++;
            position = this.hashFunction(key, i);
        }

        if(Objects.equals(this.elements[position], key))
            return position;

        return -1;
    }

    private int hashFunction(String key, int i) {
        int sumOfASCIICodes = 0;
        for(int j = 0; j < key.length(); j++) {
            sumOfASCIICodes += (int) key.charAt(j);
        }
        return (sumOfASCIICodes % capacity + i) % capacity;
    }

    @Override
    public String toString() {
        String result = "Pos  Token\n";
        for(int i = 0; i < this.capacity; i++) {
            if (this.elements[i] != null)
                result += i + "  " + this.elements[i] + "\n";
        }
        return result;
    }
}
