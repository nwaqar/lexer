public class RealDataType extends InterpreterDataType {
    private float value;

    public RealDataType(float value)
    {
        this.value = value;
    }

    @Override
    public String ToString()
    {
        return String.valueOf(value);
    }

    @Override
    public void FromString(String input)
    {
        value = Float.parseFloat(input);
    }
}
