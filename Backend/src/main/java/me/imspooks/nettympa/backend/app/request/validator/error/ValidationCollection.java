package me.imspooks.nettympa.backend.app.request.validator.error;

import java.util.ArrayList;

/**
 * Created by Nick on 20 Jun 2021.
 * Copyright © ImSpooks
 */
// Used for wildcards
public class ValidationCollection extends ArrayList<ValidationError> {

    public boolean hasError() {
        for (ValidationError validationError : this) {
            if (validationError.getMessage() != null && validationError.getMessage().length() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean containsField(String field) {
        for (ValidationError validationError : this) {
            if (validationError.getField().equals(field)) {
                return true;
            }
        }
        return false;
    }
}