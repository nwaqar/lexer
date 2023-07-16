public class VariableNode extends Node {
    private String name;
    private Token.TokenType type;
    private boolean changeable;
    private Node value;
    private int fromRange;
    private int toRange;

    public VariableNode(String name, Token.TokenType type, boolean changeable, Node value, int fromRange, int toRange)
    {
        this.name = name;
        this.type = type;
        this.changeable = changeable;
        this.value = value;
        this.fromRange = fromRange;
        this.toRange = toRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token.TokenType getType() {
        return type;
    }

    public void setType(Token.TokenType type) {
        this.type = type;
    }

    public boolean isChangeable() {
        return changeable;
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public int getFromRange() {
        return fromRange;
    }

    public void setFromRange(int fromRange) {
        this.fromRange = fromRange;
    }

    public int getToRange() {
        return toRange;
    }

    public void setToRange(int toRange) {
        this.toRange = toRange;
    }

    public Node getValue() {
        return value;
    }

    public void setValue(Node value) {
        this.value = value;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString() {
        return "VariableNode{" +
                "name='" + name + '\'' +
                ", type='" + (type != null ? type.name() : "") + '\'' +
                ", changeable=" + changeable +
                ", value=" + (value != null ? value : "") +
                ", fromRange=" + fromRange +
                ", toRange=" + toRange +
                '}';
    }
}

