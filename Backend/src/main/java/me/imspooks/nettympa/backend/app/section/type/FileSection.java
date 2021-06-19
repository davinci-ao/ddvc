package me.imspooks.nettympa.backend.app.section.type;

import lombok.Getter;
import me.imspooks.nettympa.backend.app.parser.ParserType;
import me.imspooks.nettympa.backend.app.request.validator.error.ValidationCollection;
import me.imspooks.nettympa.backend.app.section.Section;
import me.imspooks.nettympa.backend.app.section.component.Component;
import me.imspooks.nettympa.backend.app.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 *
 * This is a section file that can be used in a layout (template)
 * To create a placeholder, do the following: @placeholder("placeholder name")
 *
 * To use a variable, use this: {{variable}}
 * Example: {{user.getName()}}
 */
public class FileSection implements Section {

    @Getter private final String name;
    private final Path path;
    private final Map<String, Object> placeholders = new HashMap<>();
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String,  List<Component>> components = new HashMap<>();
    private ValidationCollection validationErrors = new ValidationCollection();

    public FileSection(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    /**
     * @return Path to the section file
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * @return Values with keys
     */
    public Map<String, Object> getPlaceholders() {
        return this.placeholders;
    }

    /**
     * Register a new placeholder
     *
     * @param key Key
     * @param value Value
     */
    public void addPlaceholder(String key, Object value) {
        this.placeholders.put(key, value);
    }

    /**
     * @return Variables linked with a key
     */
    public Map<String, Object> getVariables() {
        return variables;
    }

    /**
     * Register a new variable
     *
     * @param key Key
     * @param variable Value
     */
    public void addVariable(String key, Object variable) {
        this.variables.put(key, variable);
    }

    /**
     * @return Components linked with a key
     */
    public Map<String, List<Component>> getComponents() {
        return components;
    }

    /**
     * Register a new component
     *
     * @param key Key
     * @param component Value
     */
    public void addComponent(String key, Component component) {
        this.components.putIfAbsent(key, new ArrayList<>());
        this.components.get(key).add(component);
    }

    /**
     * @return Get all the validation errors
     */
    public ValidationCollection getValidationErrors() {
        return validationErrors;
    }

    /**
     * Set the validation errors
     */
    public void setValidationErrors(ValidationCollection validationErrors) {
        this.validationErrors = validationErrors;
    }

    public String parse() throws IOException {
        if (!Files.exists(this.path)) {
            return "<i style:\"color: #FF0000;\">Section file \"" + this.path.getFileName() + "\" does not exist.</i>";
        }

        String view = new String(Files.readAllBytes(this.path));

        view = ParserType.PLACEHOLDER.getParser().parseInput(view, this.getPlaceholders());
        view = ParserType.VARIABLES.getParser().parseInput(view, this.getVariables());
        view = ParserType.COMPONENTS.getParser().parseInput(view, this.getComponents());
        view = ParserType.ERROR.getParser().parseInput(view, this.getValidationErrors());

        { // Variables

        }

        return view;
    }

    /**
     * Empty generate method, can be overwritten
     *
     * @param session Session instance
     */
    public void generate(Session session) {

    }
}