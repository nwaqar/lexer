import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RealToInteger extends FunctionNode
{
    public RealToInteger()
    {
        super("real_to_integer");
    }

    public RealToInteger(String name, List<VariableNode> parameters, List<VariableNode> variables, List<VariableNode> constants, List<StatementBaseNode> statements)
    {
        super(name, parameters, variables, constants, statements);
    }

    /**
     * Get Real value of Integer
     * @param arguments
     */
    public void execute(Collection<InterpreterDataType> arguments)
    {
        if (arguments.size() == 1)
        {
            for (InterpreterDataType argument : arguments)
            {
                argument = new IntegerDataType(Integer.parseInt(((RealDataType) argument).ToString()));
            }
        }
    }

    @Override
    public String toString() {
        return "RealToInteger{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
