public class IntegerDataType extends InterpreterDataType {
    private int value;

    public IntegerDataType(int value) {
        this.value = value;
    }

    @Override
    public String ToString() {
        return String.valueOf(value);
    }

    @Override
    public void FromString(String input) {
        value = Integer.parseInt(input);
    }
}