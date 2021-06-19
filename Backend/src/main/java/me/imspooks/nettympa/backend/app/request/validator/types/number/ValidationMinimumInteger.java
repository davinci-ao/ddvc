package me.imspooks.nettympa.backend.app.request.validator.types.number;

import com.google.gson.JsonElement;
import lombok.Data;
import me.imspooks.nettympa.backend.app.request.validator.ValidatorOption;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
@Data
public class ValidationMinimumInteger implements ValidatorOption {

    private final long minimumValue;

    @Override
    public String runValidation(String field, JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsString().contains(".")) {
                if (element.getAsDouble() < minimumValue) {
                    return field + " is lower than the minimum value of " + minimumValue;
                }
            } else {
                if (element.getAsLong() < minimumValue) {
                    return field + " is lower than the minimum value of " + minimumValue;
                }
            }
        }
        return null;
    }
}