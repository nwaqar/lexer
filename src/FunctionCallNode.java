import java.util.List;

public class FunctionCallNode extends StatementBaseNode
{
    private String name;
    private List<ParameterNode> parameters;

    public FunctionCallNode(String name, List<ParameterNode> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterNode> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "FunctionCallNode{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}