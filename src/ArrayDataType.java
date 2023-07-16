import java.util.ArrayList;

public class ArrayDataType<T extends InterpreterDataType> extends InterpreterDataType {
    private ArrayList<T> array;

    public ArrayDataType(ArrayList<T> array) {
        this.array = array;
    }

    @Override
    public String ToString() {
        return "ArrayDataType{" +
                "array=" + array +
                '}';
    }

    @Override
    public void FromString(String input) {
        // Implement appropriate array conversion from the input string
    }
}
