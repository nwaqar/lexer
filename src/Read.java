import java.util.Collection;
import java.util.Scanner;

public class Read extends FunctionNode
{
    public Read()
    {
        super("read");
    }

    public void execute(Collection<InterpreterDataType> arguments)
    {

    }

    @Override
    public String toString() {
        return "Read{" +
                "isVariadic=" + isVariadic +
                ", left=" + left +
                ", right=" + right +
                "}\n";
    }
}
