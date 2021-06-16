package me.imspooks.nettympa.backend.app.parser;

import me.imspooks.nettympa.backend.app.parser.type.*;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public enum ParserType {
    SECTION(new SectionParser()),
    PLACEHOLDER(new PlaceholderParser()),
    VARIABLES(new VariableParser()),
    COMPONENTS(new ComponentsParser()),
    ENVIRONMENT(new EnvironmentParser()),
    ;

    private final Parser<?> parser;

    ParserType(Parser<?> parser) {
        this.parser = parser;
    }

    public Parser<?> getParser() {
        return parser;
    }
}