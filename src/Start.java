import java.util.Collection;
import java.util.Iterator;

public class Start extends FunctionNode
{
    public Start()
    {
        super("start");
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString() {
        return "Start{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
