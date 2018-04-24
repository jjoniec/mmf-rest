package pl.edu.agh.ftj.mmfrest.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.ftj.mmfrest.dto.Variable;
import pl.edu.agh.ftj.mmfrest.dto.VariablePatchData;
import pl.edu.agh.ftj.mmfrest.utils.FloatValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/variables")
public class Variables {

    private static Map<String, Variable> variablesMap = new HashMap<>();

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Variable retirieveVariable(@PathVariable(value = "name") String variableName ) {
        return (Variables.variablesMap.get(variableName));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Collection<Variable> retirieveAllVariables(){
        return (Variables.variablesMap.values());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Variable createVariable(
        @RequestParam(value = "name") String variableName,
        @RequestParam(value = "value") String valueString,
        @RequestParam(value = "frozen", defaultValue = "false") boolean isFrozen,
        @RequestParam(value = "minValue", defaultValue = "") String minValueString,
        @RequestParam(value = "maxValue", defaultValue = "") String maxValueString
        ) {

        // Validate variableName parameter
        if ((variableName == null) || (variableName.isEmpty())) {
            throw new HttpMessageNotReadableException("Variable name not specified");
        }

        if (Variables.variablesMap.containsKey(variableName)) {
            throw new HttpMessageNotReadableException("Variable " + variableName + " already exists.");
        }

        if (valueString.isEmpty()) {
            throw new HttpMessageNotReadableException("Variable value not specified");
        }

        Float value = FloatValidator.INSTANCE.validate(valueString, "Variable Value");
        Float minValue = FloatValidator.INSTANCE.validate(minValueString, "Min Value");
        Float maxValue = FloatValidator.INSTANCE.validate(maxValueString, "Max Value");

        Variable variable = null;
        try {
            variable = new Variable(variableName, value, isFrozen, minValue, maxValue);
        } catch (IllegalAccessException e) {
            throw new HttpMessageNotReadableException(e.getMessage());
        }

        variablesMap.put(variableName, variable);
        return variable;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    public Variable deleteVariable(@PathVariable(value = "name") String variableName ) {
        if ((variableName == null) || variableName.isEmpty()) {
            throw new HttpMessageNotReadableException("Variable name not specified");
        }
        return Variables.variablesMap.remove(variableName);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteVariable() {
        Variables.variablesMap.clear();
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.PATCH,
            consumes = {"application/json", "application/json;charset=UTF-8", "multipart/form-data"})
    public Variable updateVariable(
            @PathVariable(value = "name") String variableName,
            @RequestBody VariablePatchData newValues) {

        Variable variable = getVariable(variableName);

        try {
            Float newValue = newValues.getValue();
            Float newMinValue = newValues.getMinValue();
            Float newMaxValue = newValues.getMaxValue();

            if (newValue != null) {
                variable.setValue(newValue);
            }
            if (newMinValue != null) {
                variable.setMinValue(newMinValue);
            }
            if(newMaxValue != null) {
                variable.setMaxValue(newMaxValue);
            }
        } catch (IllegalAccessException e) {
            throw new HttpMessageNotReadableException(e.getMessage());
        }

        Variables.variablesMap.put(variableName, variable);
        return variable;
    }

    @RequestMapping(value = "/{name}/freeze", method = RequestMethod.POST)
    public Variable freezeVariable(@PathVariable(value = "name") String variableName) {
        Variable variable = getVariable(variableName);
        variable.freezeIt();
        Variables.variablesMap.put(variableName, variable);
        return variable;
    }

    @RequestMapping(value = "/{name}/unfreeze", method = RequestMethod.POST)
    public Variable unfreezeVariable(@PathVariable(value = "name") String variableName) {
        Variable variable = getVariable(variableName);
        variable.unfreezeIt();
        Variables.variablesMap.put(variableName, variable);
        return variable;
    }

    private Variable getVariable(String variableName) throws HttpMessageNotReadableException {
        if ((variableName == null) || variableName.isEmpty()) {
            throw new HttpMessageNotReadableException("Variable name not specified");
        }

        Variable variable = Variables.variablesMap.get(variableName);
        if (variable == null) {
            throw new HttpMessageNotReadableException("Variable: " +  variableName + " does not exist");
        }
        return variable;
    }
}