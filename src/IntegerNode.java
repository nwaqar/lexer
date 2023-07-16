public class IntegerNode extends Node {
    private int value;

    public IntegerNode(int value) {
        super();

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    float evaluate() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerNode(" + value + ')';
    }
}
