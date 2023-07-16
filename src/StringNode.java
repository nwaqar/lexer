public class StringNode extends Node
{
    private String value;

    public StringNode(String value)
    {
        super();

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString() {
        return "StringNode(" + value + ')';
    }
}
