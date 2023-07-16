import java.util.List;

public class AssignmentNode extends StatementBaseNode
{
    private List<Token> tokens;
    private VariableReferenceNode target;
    private Node value;

    public AssignmentNode(VariableReferenceNode target, Node value, List<Token> tokens) {
        this.target = target;
        this.value = value;
        this.tokens = tokens;
    }

    public Node getValue() {
        return value;
    }

    public void setValue(Node value) {
        this.value = value;
    }

    public VariableReferenceNode getTarget() {
        return target;
    }

    public Node getNodeValue() {
        return value;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "AssignmentNode{" +
                "tokens=" + tokens +
                ", target=" + target +
                ", value=" + value +
                '}';
    }
}