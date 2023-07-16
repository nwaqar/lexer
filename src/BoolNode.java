public class BoolNode extends Node
{
    private boolean value;

    public BoolNode(boolean value)
    {
        super();

        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString() {
        return "BoolNode(" + value + ')';
    }
}
