public class BooleanDataType extends InterpreterDataType {
    private boolean value;

    public BooleanDataType(boolean value) {
        this.value = value;
    }

    @Override
    public String ToString() {
        return String.valueOf(value);
    }

    @Override
    public void FromString(String input) {
        value = Boolean.parseBoolean(input);
    }
}
