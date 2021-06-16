package me.imspooks.nettympa.backend.app.parser.type;

import me.imspooks.nettympa.backend.app.parser.Parser;
import me.imspooks.nettympa.backend.settings.Environment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public class EnvironmentParser implements Parser<Environment> {

    @Override
    public String parse(String input, Environment environment) {
        Pattern pattern = Pattern.compile("@env\\(\"([^]]+?)\"\\)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);

        // find all sections
        while (matcher.find()) {
            String match = matcher.group(0);
            String target = matcher.group(1);

            input = input.replace(match, environment.getValue(target));
        }

        return input;
    }
}