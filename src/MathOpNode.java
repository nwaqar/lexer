public class MathOpNode extends Node
{
    private String value;

    public MathOpNode(String value, Node left, Node right) {
        super(left, right);

        this.value = value;
    }

    public MathOpNode(String value, Node operand) {
        super(operand, null);

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
        float leftValue = left.evaluate();
        float rightValue = right.evaluate();

        switch (value) {
            case "+":
                return leftValue + rightValue;
            case "-":
                return leftValue - rightValue;
            case "*":
                return leftValue * rightValue;
            case "/":
                return leftValue / rightValue;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "MathOpNode" +
                "(" + this.value +
                ", " + super.left +
                ", " + super.right +
                ')';
    }
}
