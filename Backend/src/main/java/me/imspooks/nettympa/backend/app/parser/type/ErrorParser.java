package me.imspooks.nettympa.backend.app.parser.type;

import me.imspooks.nettympa.backend.app.parser.Parser;
import me.imspooks.nettympa.backend.app.request.validator.error.ValidationError;
import me.imspooks.nettympa.backend.app.request.validator.error.ValidationCollection;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public class ErrorParser implements Parser<ValidationCollection> {

    @Override
    public String parse(String view, ValidationCollection collection) {
        { // Error messages
            Pattern pattern = Pattern.compile("@errormsg\\(\"([^]]+?)\"\\)", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(view);

            // find all sections
            while (matcher.find()) {
                String match = matcher.group(0);
                String target = matcher.group(1);

                for (ValidationError error : collection) {
                    if (error.getMessage() != null && error.getMessage().length() > 0 && error.getField().equals(target)) {

                        view = view.replace(match, error.getMessage());
                    }
                }
                view = view.replace(match, "");
            }
        }

        { // Old values
            Pattern pattern = Pattern.compile("@oldval\\(\"([^]]+?)\"\\)", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(view);

            // find all sections
            while (matcher.find()) {
                String match = matcher.group(0);
                String target = matcher.group(1);

                for (ValidationError error : collection) {
                    if (error.getField().equals(target)) {

                        view = view.replace(match, error.getElement().getAsString());
                    }
                }
                view = view.replace(match, "");
            }
        }

        return view;
    }
}