import java.util.Collection;
import java.util.Iterator;

public class Left extends FunctionNode
{
    public Left()
    {
        super("left");
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString()
    {
        return "Left{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
