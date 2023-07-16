public class VariableReferenceNode extends Node
{
    private String name;
    private Node index;

    public VariableReferenceNode(String name, Node index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public Node getIndex() {
        return index;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString() {
        return "VariableReferenceNode{" +
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }
}