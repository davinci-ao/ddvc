package me.imspooks.nettympa.backend.settings;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Nick on 31 jan. 2020.
 * Copyright © ImSpooks
 */
public interface Settings {

    /**
     * Gson instance
     */
    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.hasModifier(Modifier.STATIC) || fieldAttributes.hasModifier(Modifier.TRANSIENT);
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }).create();


    /**
     * Load a settings instance from a file
     *
     * @param path Target path of file
     * @param clazz Settings type class
     * @return Specified settings class
     * @throws IOException if the file cannot be found
     * @throws JsonSyntaxException if the file isn't a valid json
     */
    static <T extends Settings> T load(Path path, Class<T> clazz, Gson gson) throws IOException, JsonSyntaxException {
        path = path.toAbsolutePath();
        T result = gson.fromJson(new String(Files.readAllBytes(path), StandardCharsets.UTF_8), clazz);
        save(path, result, gson);
        return result;
    }

    /**
     * Load a settings instance from a file
     * @see Settings#load(Path, Class, Gson)
     */
    static <T extends Settings> T load(Path path, Class<T> clazz) throws IOException, JsonSyntaxException {
        return load(path, clazz, gson);
    }

    /**
     * Saves a settings instance to a file
     *
     * @param path Target path
     * @param settings Settings instance
     * @throws IOException if the file cannot be created
     */
    static void save(Path path, Settings settings, Gson gson) throws IOException {
        path = path.toAbsolutePath();
        if (!Files.exists(path)) {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.createFile(path);
        }

        Files.write(path, gson.toJson(settings).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Saves a settings instance to a file
     * @see Settings#save(Path, Settings, Gson)
     */
    static void save(Path path, Settings settings) throws IOException {
        save(path, settings, gson);
    }
}