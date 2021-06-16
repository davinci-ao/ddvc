package me.imspooks.nettympa.backend.adapters;

import com.google.gson.JsonElement;
import lombok.Getter;
import me.imspooks.nettympa.backend.global.Global;

import java.util.function.Function;

/**
 * Created by Nick on 14 aug. 2020.
 * Copyright © ImSpooks
 */
@Getter
enum StoredDataType {

    STRING("string", String.class),
    BYTE("byte", Byte.class),
    SHORT("short", Short.class),
    INTEGER("integer", Integer.class),
    LONG("long", Long.class),
    FLOAT("float", Float.class),
    DOUBLE("double", Double.class),
    BOOLEAN("boolean", Boolean.class),
    CHARACTER("character", Character.class),
    ;

    private final String storedName;
    private final Class<?> clazz;

    private final Function<Object, JsonElement> write;
    private final Function<JsonElement, Object> read;

    static StoredDataType[] CACHE = values();

    StoredDataType(String storedName, Class<?> clazz, Function<Object, JsonElement> write, Function<JsonElement, Object> read) {
        this.storedName = storedName;
        this.clazz = clazz;
        this.write = write;
        this.read = read;
    }

    StoredDataType(String storedName, Class<?> clazz) {
        this(
                storedName,
                clazz,
                write -> Global.GSON.toJsonTree(write),
                read -> Global.GSON.fromJson(read, clazz)
        );
    }

    public static StoredDataType getFromObject(Class<?> clazz) {
        for (StoredDataType storedDataType : CACHE) {
            if (storedDataType.getClazz().isAssignableFrom(clazz)) {
                return storedDataType;
            }
        }
        return null;
    }

    public static StoredDataType getFromObject(Object input) {
        return getFromObject(input.getClass());
    }

    public static StoredDataType getFromType(String type) {
        for (StoredDataType storedDataType : CACHE) {
            if (storedDataType.getStoredName().equalsIgnoreCase(type)) {
                return storedDataType;
            }
        }
        return null;
    }
}