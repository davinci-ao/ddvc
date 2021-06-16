package me.imspooks.nettympa.backend.app.layout;

import java.nio.file.Path;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public interface Layout {

    /**
     * Gets the layout HTML file
     *
     * To create a section, put @section("section name") in the file
     * and replace it in the view sub class
     *
     * To include a file, put @include("file from root folder") in the file.
     * It will replace the content of the target file with this include string
     *
     * @return Path to the layout file
     */
    Path getLayoutFile();
}