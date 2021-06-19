package me.imspooks.nettympa.backend.app.request.validator.error;

import com.google.gson.JsonElement;

/**
 * Created by Nick on 19 Jun 2021.
 * Copyright © ImSpooks
 */
public class ValidationError {

    private String field;
    private JsonElement element;
    private String message;

    /**
     * Validation Error
     *
     * @param field Field to validate
     * @param element Element to validate
     * @param message Error message
     */
    public ValidationError(String field, JsonElement element, String message) {
        this.field = field;
        this.element = element;
        this.message = message;
    }

    /**
     * @return Field to validate
     */
    public String getField() {
        return field;
    }

    /**
     * @return Element to validate
     */
    public JsonElement getElement() {
        return element;
    }

    /**
     * @return Error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return formatted toString method
     */
    @Override
    public String toString() {
        return "ValidationError{" +
                "field='" + field + '\'' +
                ", element=" + element +
                ", error='" + message + '\'' +
                '}';
    }
}