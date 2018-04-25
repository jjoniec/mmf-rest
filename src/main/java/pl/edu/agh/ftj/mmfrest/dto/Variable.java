package pl.edu.agh.ftj.mmfrest.dto;

import java.util.Objects;

public class Variable {
    private final String name;
    private Float value = null;
    private boolean isFrozen = false;
    private Float minValue = null;
    private Float maxValue = null;

    public Variable(String name, Float value) throws IllegalAccessException {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalAccessException("Variable name must be specified");
        }

        if (value == null) {
            throw new IllegalAccessException("Variable value must be specified");
        }
        this.name = name;
        this.value = value;
    }


    public Variable(String name, Float value, boolean isFrozen) throws IllegalAccessException {
        this(name, value);
        this.isFrozen = isFrozen;
    }

    public Variable(String name, Float value, boolean isFrozen, Float minValue, Float maxValue) throws IllegalAccessException {
        this(name, value, isFrozen);
        validateMinValue(minValue);
        validateMaxValue(maxValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getName() {
        return name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(float value) throws IllegalAccessException {
        frozenCheck();
        this.value = value;
    }

    public Boolean isFrozen() {
        return isFrozen;
    }
    public void freezeIt() {isFrozen = true;}
    public void unfreezeIt() { isFrozen = false; }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) throws IllegalAccessException{
        frozenCheck();
        validateMinValue(minValue);
        this.minValue = minValue;
    }

    public void removeMinValue() throws IllegalAccessException{
        frozenCheck();
        this.minValue = null;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) throws IllegalAccessException{
        frozenCheck();
        validateMaxValue(maxValue);
        this.maxValue = maxValue;
    }

    public void removeMaxValue() throws IllegalAccessException {
        frozenCheck();
        this.maxValue = null;
    }

    private void validateMinValue(Float minValue) throws IllegalAccessException {
        if (minValue == null) return;

        if (minValue > this.value) {
            throw new IllegalAccessException("Min Value (" + minValue + ") cannot be bigger than current value (" + this.getValue() + ")");
        }

        if ((maxValue != null) && (minValue > maxValue)) {
            throw new IllegalAccessException("Min Value (" + minValue + ") cannot be bigger than Max Value (" + this.getMaxValue() + ")");
        }
    }

    private void validateMaxValue(Float maxValue) throws IllegalAccessException {
        if (maxValue == null) return;

        if (maxValue < this.getValue()) {
            throw new IllegalAccessException("Max Value (" + maxValue + ") cannot be smaller than current value (" + this.getValue() + ")");
        }

        if ((this.getMinValue() != null) && (this.getMinValue() > maxValue)) {
            throw new IllegalAccessException("Max Value (" + maxValue + ") cannot be smaller than current Min Value (" + this.getMinValue() + ")");
        }
    }

    private void frozenCheck() throws IllegalAccessException {
        if (isFrozen == true) {
            throw new IllegalAccessException("Variable " + name + " is frozen and cannot be modified");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return Objects.equals(getValue(), variable.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
    }
}
