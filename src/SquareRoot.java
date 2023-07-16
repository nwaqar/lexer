import java.util.Collection;
import java.util.List;

public class SquareRoot extends FunctionNode
{
    public SquareRoot()
    {
        super("square_root");
    }

    public SquareRoot(String name, List<VariableNode> parameters, List<VariableNode> variables, List<VariableNode> constants, List<StatementBaseNode> statements)
    {
        super(name, parameters, variables, constants, statements);
    }

    /**
     * Get square root of argument
     * @param arguments
     */
    public void execute(Collection<InterpreterDataType> arguments)
    {
        if (arguments.size() == 1)
        {
            for (InterpreterDataType argument : arguments)
            {
                String value = ((IntegerDataType) argument).ToString();
                double doubleValue = Math.sqrt(Double.parseDouble(value));

                argument = new RealDataType((float) doubleValue);
            }
        }
    }

    @Override
    public String toString() {
        return "SquareRoot{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
