package me.imspooks.nettympa.backend.app.request.validator;

import com.google.gson.JsonElement;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.request.validator.error.ValidationError;
import me.imspooks.nettympa.backend.app.request.validator.error.ValidationCollection;

import java.util.Map;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
public class Validator {

    private ValidationField[] fields;

    /**
     * Validator constructor
     *
     * @param fields Validation fields
     */
    public Validator(ValidationField... fields) {
        this.fields = fields;
    }

    /**
     * @return Validation fields
     */
    public ValidationField[] getOptions() {
        return fields;
    }

    /**
     * Validation executor on form element
     *
     * @param request Request to read from
     * @return Old value and error message
     */
    public ValidationCollection runValidation(Request request) {
        ValidationCollection response = new ValidationCollection();

        for (ValidationField field : fields) {
            // Get JsonElement from request
            JsonElement element = request.get(field.getField());
            for (ValidatorOption option : field.getOptions()) {
                // Run validation option
                String errorMsg = option.runValidation(field.getField(), element);
                if (errorMsg != null && errorMsg.length() > 0) {
                    response.add(new ValidationError(field.getField(), element, errorMsg));
                }
            }
        }

        // Add empty errors, used for resetting old values
        for (Map.Entry<String, JsonElement> entry : request.getValues().entrySet()) {
            if (!response.containsField(entry.getKey())) {
                response.add(new ValidationError(entry.getKey(), entry.getValue(), null));
            }
        }

        return response;
    }
}