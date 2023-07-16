public class RealNode extends Node {
    private float value;

    public RealNode(float value)
    {
        super();

        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    float evaluate() {
        return value;
    }

    @Override
    public String toString() {
        return "RealNode(" + value + ')';
    }
}
