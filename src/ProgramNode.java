import java.util.HashMap;

public class ProgramNode extends Node
{
    private HashMap<String, FunctionNode> functionNodes;

    public ProgramNode()
    {
        functionNodes = new HashMap<>();
    }

    @Override
    float evaluate() {
        return 0;
    }

    public void addFunctionNode(FunctionNode node)
    {
        functionNodes.put(node.getName(), node);
    }

    public FunctionNode getFunctionNode(String functionName)
    {
        return functionNodes.get(functionName);
    }

    public HashMap<String, FunctionNode> getFunctionNodes() {
        return functionNodes;
    }

    public void setFunctionNodes(HashMap<String, FunctionNode> functionNodes) {
        this.functionNodes = functionNodes;
    }

    @Override
    public String toString() {
        return "\nProgramNode{\n" +
                "functionNodes=" + functionNodes +
                '}';
    }
}
