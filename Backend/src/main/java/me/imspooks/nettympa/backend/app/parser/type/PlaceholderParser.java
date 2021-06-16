package me.imspooks.nettympa.backend.app.parser.type;

import me.imspooks.nettympa.backend.app.parser.Parser;
import me.imspooks.nettympa.backend.app.section.Section;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public class PlaceholderParser implements Parser<Map<String, Object>> {

    @Override
    public String parse(String view, Map<String, Object> placeholders) throws IOException {
        Pattern pattern = Pattern.compile("@placeholder\\(\"([^]]+?)\"\\)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(view);

        // find all sections
        while (matcher.find()) {
            String match = matcher.group(0);
            String target = matcher.group(1);

            for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
                if (entry.getKey().equals(target)) {
                    view = view.replace(match, entry.getValue() instanceof Section ? ((Section) entry.getValue()).parse() : entry.getValue().toString());
                }
            }
            view = view.replace(match, "");
        }

        return view;
    }
}