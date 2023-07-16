public class BooleanCompareNode extends Node
{
    private Token.TokenType tokenType;
    private Node left;
    private Node right;

    public BooleanCompareNode(Token.TokenType tokenType, Node left, Node right) {
        this.tokenType = tokenType;
        this.left = left;
        this.right = right;
    }

    public Token.TokenType getCompareType() {
        return tokenType;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString() {
        return "BooleanCompareNode{" +
                "tokenType=" + tokenType.name() +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}