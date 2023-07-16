import java.util.Collection;
import java.util.Iterator;

public class Substring extends FunctionNode
{
    public Substring()
    {
        super("substring");
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString() {
        return "Substring{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
