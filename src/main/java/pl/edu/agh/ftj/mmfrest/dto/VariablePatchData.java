package pl.edu.agh.ftj.mmfrest.dto;

public class VariablePatchData {
    private Float value;
    private Float minValue;
    private Float maxValue;

    public VariablePatchData() {
        this.value = null;
        this.minValue = null;
        this.maxValue = null;
    }

   public void setValue(Float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }
}
