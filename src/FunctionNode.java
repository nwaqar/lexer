import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FunctionNode extends Node
{
    private String name;
    private List<VariableNode> parameters;
    private List<VariableNode> variables;
    private List<VariableNode> constants;
    private List<StatementBaseNode> statements;
    protected boolean isVariadic;

    public FunctionNode(String name) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.statements = new ArrayList<>();

        if (this.name.equals("write") || this.name.equals("read"))
        {
            this.isVariadic = true;
        }
        else
        {
            this.isVariadic = false;
        }
    }

    public FunctionNode(String name, List<VariableNode> parameters, List<VariableNode> variables, List<VariableNode> constants, List<StatementBaseNode> statements) {
        this.name = name;
        this.parameters = parameters;
        this.variables = variables;
        this.constants = constants;
        this.statements = statements;
    }

    public String getName()
    {
        return name;
    }

    public List<VariableNode> getParameters()
    {
        return parameters;
    }

    public List<VariableNode> getVariables()
    {
        return variables;
    }

    public List<VariableNode> getConstants()
    {
        return constants;
    }

    public List<StatementBaseNode> getStatements()
    {
        return statements;
    }

    public void setStatements(List<StatementBaseNode> statements) {
        this.statements = statements;
    }

    public boolean isVariadic()
    {
        return isVariadic;
    }

    @Override
    float evaluate() {
        return 0;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("(\n")
            .append("isVariadic=")
            .append(isVariadic)
            .append("\n");

        for (int i = 0; i < parameters.size(); i++)
        {
            sb.append(parameters.get(i));
            if (i < parameters.size() - 1)
            {
                sb.append(", ");
            }
        }

        sb.append(") {\n");

        for (VariableNode variable : variables)
        {
            sb.append(variable).append("\n");
        }

        for (VariableNode constant : constants)
        {
            sb.append(constant).append("\n");
        }

        for (Node statement : statements)
        {
            sb.append(statement).append("\n");
        }

        sb.append("}\n");
        return sb.toString();
    }
}



