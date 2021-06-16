package me.imspooks.nettympa.backend.files;

import lombok.Setter;
import me.imspooks.nettympa.backend.Mpa;
import me.imspooks.nettympa.backend.app.response.ResponseType;
import me.imspooks.nettympa.backend.app.response.type.RawResponse;
import me.imspooks.nettympa.backend.logger.MpaLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class FileManager {

    @Setter private static Mpa instance;

    public static Path getFromPublic(String path) {
        return instance.getRoot().resolve("public").resolve(path);
    }

    public static Path getFromView(String path) {
        return instance.getRoot().resolve("view").resolve(path);
    }

    public static RawResponse toResponse(Path path) {
        if (Files.exists(path)) {
            // Converts the result into a byte array
            String extension = "";
            int i = path.getFileName().toString().lastIndexOf('.');
            if (i > 0) {
                extension = path.getFileName().toString().substring(i+1);
            }

            for (ResponseType type : ResponseType.CACHE) {
                for (String extensions : type.getExtensions()) {
                    if (extensions.equalsIgnoreCase(extension)) {
                        try {
                            return new RawResponse(type, Files.readAllBytes(path));
                        } catch (IOException e) {
                            MpaLogger.getLogger().warn(String.format("Could not read file data for file \"%s\"",  path.toString()), e);
                        }
                    }
                }
            }
        }
        return new RawResponse(ResponseType.HTML, "null".getBytes());
    }
}