package pl.edu.agh.ftj.mmfrest.utils;

import org.springframework.http.converter.HttpMessageNotReadableException;

public enum FloatValidator {
    INSTANCE;

    public Float validate(String stringValue, String parameterName) throws HttpMessageNotReadableException {
        Float value = null;
        if ((stringValue==null) || stringValue.isEmpty()) {
            // Nothing to validate and convert
            return value;
        }

        try {
            value = Float.parseFloat(stringValue);
        }
        catch (NumberFormatException ex)  {
            throw new HttpMessageNotReadableException(parameterName + " is not a number");
        }
        return value;
    }

    public void validateMinAndMax(float minValue, float maxValue) throws HttpMessageNotReadableException {
        if (minValue > maxValue) {
            throw new HttpMessageNotReadableException("Min Value bigger than Max Value");
        }

        if (minValue == maxValue) {
            throw new HttpMessageNotReadableException("Min Value and Max Value are the same");
        }
    }

    public void validateRange (float value, float minValue, float maxValue) throws HttpMessageNotReadableException {
        if (value < minValue) {
            throw new HttpMessageNotReadableException("Value smaller than Min Value");
        }

        if (value > maxValue) {
            throw new HttpMessageNotReadableException("Value bigger than Max Value");
        }
    }
}
