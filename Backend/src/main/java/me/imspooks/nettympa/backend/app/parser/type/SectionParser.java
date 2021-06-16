package me.imspooks.nettympa.backend.app.parser.type;

import me.imspooks.nettympa.backend.app.parser.Parser;
import me.imspooks.nettympa.backend.app.section.Section;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public class SectionParser implements Parser<Collection<Section>> {

    @Override
    public String parse(String layout, Collection<Section> sections) throws IOException {
        for (Section section : sections) {
            layout = layout.replace(section.getName(), section.parse());
        }

/*        Pattern pattern = Pattern.compile("@section\\(\"([^]]+?)\"\\)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(layout);

        // find all sections
        while (matcher.find()) {
            String match = matcher.group(0);
            String target = matcher.group(1);

            for (Section section : sections) {
                if (section.getName().equals(target)) {
                    layout = layout.replace(match, section.parse());
                }
            }
            layout = layout.replace(match, "");
        }*/

        return layout;
    }
}