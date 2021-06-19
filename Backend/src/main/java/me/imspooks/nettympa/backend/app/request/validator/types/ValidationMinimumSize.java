package me.imspooks.nettympa.backend.app.request.validator.types;

import com.google.gson.JsonElement;
import lombok.Data;
import me.imspooks.nettympa.backend.app.request.validator.ValidatorOption;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
@Data
public class ValidationMinimumSize implements ValidatorOption {

    private final int minimumLength;

    @Override
    public String runValidation(String field, JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsString().length() < this.minimumLength) {
            return field + " must be a minimum length of " + this.minimumLength + " characters";
        } else if (element.isJsonArray() && element.getAsJsonArray().size() < this.minimumLength) {
            return field + " must be a minimum length of " + this.minimumLength + " elements";
        }
        return null;
    }
}