public class StringDataType extends InterpreterDataType {
    private String value;

    public StringDataType(String value) {
        this.value = value;
    }

    @Override
    public String ToString() {
        return value;
    }

    @Override
    public void FromString(String input) {
        value = input;
    }
}
