import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GetRandom extends FunctionNode
{
    private final Random random = new Random();

    public GetRandom()
    {
        super("get_randon");
    }

    public GetRandom(String name, List<VariableNode> parameters, List<VariableNode> variables, List<VariableNode> constants, List<StatementBaseNode> statements)
    {
        super(name, parameters, variables, constants, statements);
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString() {
        return "GetRandom{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
