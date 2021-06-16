package me.imspooks.nettympa.backend.app.parser.type;

import me.imspooks.nettympa.backend.app.parser.Parser;
import me.imspooks.nettympa.backend.app.section.Section;
import me.imspooks.nettympa.backend.app.section.component.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public class ComponentsParser implements Parser<Map<String, List<Component>>> {

    @Override
    public String parse(String view, Map<String, List<Component>> components) throws IOException {
        for (Map.Entry<String, List<Component>> entry : components.entrySet()) {
            String match = "@component(\"" + entry.getValue() + "\")";

            StringBuilder builder = new StringBuilder();

            for (Component component : entry.getValue()) {
                builder.append(component.parse());
            }

            view = view.replace(match, builder.toString());
        }

/*        Pattern pattern = Pattern.compile("@component\\(\"([^]]+?)\"\\)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(view);

        // find all sections
        while (matcher.find()) {
            String match = matcher.group(0);
            String target = matcher.group(1);

            for (Map.Entry<String, List<Component>> entry : components.entrySet()) {
                if (entry.getKey().equals(target)) {
                    StringBuilder builder = new StringBuilder();

                    for (Component component : entry.getValue()) {
                        builder.append(component.parse());
                    }

                    view = view.replace(match, builder.toString());
                }
            }
            view = view.replace(match, "");
        }*/

        return view;
    }
}