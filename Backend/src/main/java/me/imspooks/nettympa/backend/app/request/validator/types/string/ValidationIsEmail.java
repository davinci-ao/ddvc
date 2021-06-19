package me.imspooks.nettympa.backend.app.request.validator.types.string;

import com.google.gson.JsonElement;
import lombok.Data;
import me.imspooks.nettympa.backend.app.request.validator.ValidatorOption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 17 Jun 2021.
 * Copyright © ImSpooks
 */

public class ValidationIsEmail implements ValidatorOption {

    @Override
    public String runValidation(String field, JsonElement element) {
        if (element.isJsonPrimitive()) {
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(element.getAsString());

            if(!mat.matches()){
                return "Must be an email address";
            }
        }
        return null;
    }
}