import java.util.List;

public class ParameterNode extends StatementBaseNode
{
    private VariableReferenceNode target;
    private Node value; // expression

    public ParameterNode(VariableReferenceNode target, Node value) {
        this.target = target;
        this.value = value;
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

    @Override
    public String toString() {
        return "ParameterNode{" +
                "target=" + target +
                ", value=" + value +
                '}';
    }
}