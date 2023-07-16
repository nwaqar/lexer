import java.util.ArrayList;
import java.util.List;

public class StatementBaseNode extends Node
{
    private List<Token> tokens;
    private Node node;

    public StatementBaseNode() {
        tokens = new ArrayList<>();
    }

    @Override
    float evaluate() {
        return 0;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "StatementBaseNode{" +
                "tokens=" + tokens +
                ", node=" + node +
                '}';
    }
}
