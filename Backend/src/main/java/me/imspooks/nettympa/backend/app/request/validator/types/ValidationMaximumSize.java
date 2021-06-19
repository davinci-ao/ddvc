package me.imspooks.nettympa.backend.app.request.validator.types;

import com.google.gson.JsonElement;
import lombok.Data;
import me.imspooks.nettympa.backend.app.request.validator.ValidatorOption;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
@Data
public class ValidationMaximumSize implements ValidatorOption {

    private final int maximumLength;

    @Override
    public String runValidation(String field, JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsString().length() > this.maximumLength) {
            return field + " must be a maximum length of " + this.maximumLength + " characters";
        } else if (element.isJsonArray() && element.getAsJsonArray().size() > this.maximumLength) {
            return field + " must be a maximum length of " + this.maximumLength + " elements";
        }
        return null;
    }
}