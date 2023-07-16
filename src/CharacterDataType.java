public class CharacterDataType extends InterpreterDataType {
    private char value;

    public CharacterDataType(char value) {
        this.value = value;
    }

    @Override
    public String ToString() {
        return String.valueOf(value);
    }

    @Override
    public void FromString(String input) {
        value = input.charAt(0);
    }
}
