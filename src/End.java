import java.util.Collection;
import java.util.Iterator;

public class End extends FunctionNode
{
    public End()
    {
        super("end");
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString()
    {
        return "End{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
