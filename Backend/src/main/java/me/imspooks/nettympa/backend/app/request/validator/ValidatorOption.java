package me.imspooks.nettympa.backend.app.request.validator;

import com.google.gson.JsonElement;
import me.imspooks.nettympa.backend.util.Pair;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */
//TODO documentation
public interface ValidatorOption {

    /**
     * Validation executor on form element
     *
     * @param element Element to validate
     * @return Error message
     */
    String runValidation(String field, JsonElement element);
}