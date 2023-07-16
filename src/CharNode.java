public class CharNode extends Node
{
    private char value;

    public CharNode(char value)
    {
        super();

        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString() {
        return "CharNode(" + value + ')';
    }
}
