import java.util.Collection;
import java.util.Iterator;

public class Right extends FunctionNode
{
    public Right()
    {
        super("right");
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString() {
        return "Right{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
