import java.util.List;

public class ForNode extends StatementBaseNode
{
    private List<Token> tokens;
    private Node from;
    private Node to;
    private List<StatementBaseNode> statements;

    public ForNode(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ForNode(VariableReferenceNode loopVariable, Node from, Node to, List<StatementBaseNode> statements) {
        super();

        this.from = from;
        this.to = to;
        this.statements = statements;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public List<StatementBaseNode> getStatements() {
        return statements;
    }

    public void setStatements(List<StatementBaseNode> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "ForNode{" +
                "tokens=" + tokens +
                ", from=" + from +
                ", to=" + to +
                ", statements=" + statements +
                '}';
    }
}