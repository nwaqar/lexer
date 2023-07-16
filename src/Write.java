import java.util.Collection;

/**
 * Built-in write function for Shank
 */
public class Write extends FunctionNode
{
    public Write()
    {
        super("write");
    }

    /**
     * Prints arguments to console
     * @param arguments
     */
    public void execute(Collection<InterpreterDataType> arguments)
    {
        for (InterpreterDataType argument : arguments)
        {
            System.out.print(argument.ToString());
        }

        System.out.println();
    }

    @Override
    public String toString() {
        return "Write{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
