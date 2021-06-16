package me.imspooks.nettympa.backend.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.*;

/**
 * Created by Nick on 11 May 2020.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public class CliUtil {

    private CommandLine line;
    private Options options = new Options();

    /**
     * Add an option
     *
     * @param opt Target option
     * @return same instance
     */
    public CliUtil addOption(Option opt) {
        this.options.addOption(opt);
        return this;
    }

    /**
     * Parses the given options with given input
     *
     * @param input Input to be parsed
     * @return same instance
     */
    public CliUtil parse(String[] input) throws ParseException {
        this.line = new DefaultParser().parse(options, input);
        return this;
    }

    /**
     * Get a value from an option
     *
     * @param option Target option
     * @return Option value as a {@code String}
     * @throws AssertionError when line has not been parsed yet
     */
    public String get(String option) {
        check();
        return line.getOptionValue(option);
    }

    /**
     * Uses {@code option.getOpt()}
     *
     * @throws AssertionError when line has not been parsed yet
     * @see CliUtil#get(String)
     */
    public String get(Option option) {
        return this.get(option.getOpt());
    }

    /**
     * Get a value from an option
     *
     * @param option     Target option
     * @param defaultVal Default value if the option doesnt exist
     * @return Option value as a {@code String}
     * @throws AssertionError when line has not been parsed yet
     */
    public String getOrDefault(String option, String defaultVal) {
        check();
        return this.hasOption(option) ? line.getOptionValue(option) : defaultVal;
    }

    /**
     * Uses {@code option.getOpt()}
     *
     * @throws AssertionError when line has not been parsed yet
     * @see CliUtil#getOrDefault(String, String)
     */
    public String getOrDefault(Option option, String defaultVal) {
        return this.getOrDefault(option.getOpt(), defaultVal);
    }

    /**
     * Checks if an option exists
     *
     * @param option Target option
     * @return {@code true} if the option exists, {@code false} otherwise
     * @throws AssertionError when line has not been parsed yet
     */
    public boolean hasOption(String option) {
        check();
        return line.hasOption(option);
    }

    /**
     * @throws AssertionError when line has not been parsed yet
     * @see CliUtil#hasOption(String)
     */
    public boolean hasOption(Option option) {
        return this.hasOption(option.getOpt());
    }

    /**
     * Check if the line has been parsed
     *
     * @throws AssertionError when line has not been parsed yet
     */
    private void check() {
        if (line == null)
            throw new AssertionError("Values haven't been parsed yet");
    }
}