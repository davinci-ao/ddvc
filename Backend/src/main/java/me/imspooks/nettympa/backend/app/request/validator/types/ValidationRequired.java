package me.imspooks.nettympa.backend.app.request.validator.types;

import com.google.gson.JsonElement;
import me.imspooks.nettympa.backend.app.request.validator.ValidatorOption;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
public class ValidationRequired implements ValidatorOption {

    private static final String ERROR_MSG = "This field cannot be empty";

    @Override
    public String runValidation(String field, JsonElement element) {
        if (element.isJsonNull()) {
            return ERROR_MSG;
        } else if (element.isJsonPrimitive() && element.getAsString().length() == 0) {
            return ERROR_MSG;
        } else if (element.isJsonArray() && element.getAsJsonArray().size() == 0) {
            return ERROR_MSG;
        }
        return null;
    }
}