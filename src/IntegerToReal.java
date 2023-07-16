import java.util.Collection;
import java.util.List;

public class IntegerToReal extends FunctionNode
{
    public IntegerToReal()
    {
        super("integer_to_real");
    }

    public IntegerToReal(String name, List<VariableNode> parameters, List<VariableNode> variables, List<VariableNode> constants, List<StatementBaseNode> statements)
    {
        super(name, parameters, variables, constants, statements);
    }

    /**
     * Get Integer value of Real
     * @param arguments
     */
    public void execute(Collection<InterpreterDataType> arguments)
    {
        if (arguments.size() == 1)
        {
            for (InterpreterDataType argument : arguments)
            {
                argument = new RealDataType(Float.parseFloat(((IntegerDataType) argument).ToString()));
            }
        }
    }

    @Override
    public String toString()
    {
        return "IntegerToReal{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
