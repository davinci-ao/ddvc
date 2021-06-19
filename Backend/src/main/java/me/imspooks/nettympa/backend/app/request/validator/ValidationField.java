package me.imspooks.nettympa.backend.app.request.validator;

/**
 * Created by Nick on 19 Jun 2021.
 * Copyright © ImSpooks
 */
public class ValidationField {

    private String field;
    private ValidatorOption[] options;

    /**
     * ValidationField constructor
     *
     * @param field Field to validate
     * @param options Validation options
     */
    public ValidationField(String field, ValidatorOption... options) {
        this.field = field;
        this.options = options;
    }

    /**
     * @return Field to validate
     */
    public String getField() {
        return field;
    }

    /**
     * @return Validation options
     */
    public ValidatorOption[] getOptions() {
        return options;
    }
}