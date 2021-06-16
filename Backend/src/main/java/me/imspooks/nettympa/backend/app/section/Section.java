package me.imspooks.nettympa.backend.app.section;


import java.io.IOException;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public interface Section {

    /**
     * @return Name of the section
     */
    String getName();

    /**
     * Parse this section
     *
     * @return Parsed section as string
     * @throws IOException When failed
     */
    String parse() throws IOException;


}