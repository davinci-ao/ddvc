package me.imspooks.nettympa.backend.app.request.validator.types.number;

import com.google.gson.JsonElement;
import lombok.Data;
import me.imspooks.nettympa.backend.app.request.validator.ValidatorOption;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
@Data
public class ValidationMaximumDouble implements ValidatorOption {

    private final double maximumValue;

    @Override
    public String runValidation(String field, JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsString().contains(".")) {
                if (element.getAsDouble() > maximumValue) {
                    return field + " is higher than the maximum value of " + maximumValue;
                }
            } else {
                if (element.getAsLong() > maximumValue) {
                    return field + " is higher than the maximum value of " + maximumValue;
                }
            }
        }
        return null;
    }
}