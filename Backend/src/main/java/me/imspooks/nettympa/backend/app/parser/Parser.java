package me.imspooks.nettympa.backend.app.parser;

import java.io.IOException;

/**
 * Created by Nick on 03 jul. 2020.
 * Copyright © ImSpooks
 */
public interface Parser<T> {

    @SuppressWarnings("unchecked")
    default String parseInput(String input, Object object) throws IOException {
        return this.parse(input, (T) object);
    }

    String parse(String input, T object) throws IOException;
}