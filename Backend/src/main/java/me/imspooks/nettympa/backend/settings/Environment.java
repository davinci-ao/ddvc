package me.imspooks.nettympa.backend.settings;

import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.nio.file.Path;

/**
 * Created by Nick on 29 Jun 2020.
 * Copyright © ImSpooks
 *
 * Environment variables are stored in here
 */
@ToString
public class Environment implements Settings {

    /**
     * Title of the page
     */
    @Getter private String title = "Netty-MPA";

    /**
     * Database settings
     */
    @Getter private DatabaseSettings databaseSettings = new DatabaseSettings();


    public String getValue(String key) {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.getName().equalsIgnoreCase(key)) {
                declaredField.setAccessible(true);
                try {
                    return declaredField.get(this).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return System.getenv(key);
    }

    public static Environment loadEnvironment(Path root) {
        try {
            return Settings.load(root.resolve(".env"), Environment.class);
        } catch (Exception e) {
            Environment environment = new Environment();

            try {
                Settings.save(root.resolve(".env"), environment);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return environment;
        }
    }
}