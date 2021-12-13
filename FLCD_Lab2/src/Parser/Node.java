package Parser;

public class Node {
    private String value;
    private Integer index;
    private Integer fatherIndex;
    private Integer rightSiblingIndex;

    public Node() {
        this.rightSiblingIndex = 0;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getFatherIndex() {
        return fatherIndex;
    }

    public void setFatherIndex(Integer fatherIndex) {
        this.fatherIndex = fatherIndex;
    }

    public Integer getRightSiblingIndex() {
        return rightSiblingIndex;
    }

    public void setRightSiblingIndex(Integer rightSiblingIndex) {
        this.rightSiblingIndex = rightSiblingIndex;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", index=" + index +
                ", fatherIndex=" + fatherIndex +
                ", rightSiblingIndex=" + rightSiblingIndex +
                '}';
    }
}
