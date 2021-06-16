package me.imspooks.nettympa.backend.global;

import com.google.gson.*;
import me.imspooks.nettympa.backend.adapters.SessionAdapter;
import me.imspooks.nettympa.backend.app.session.Session;

import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Nick on 02 Jun 2020.
 * Copyright © ImSpooks
 */
public class Global {

    private static final GsonBuilder BUILDER = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.hasModifier(Modifier.STATIC) || fieldAttributes.hasModifier(Modifier.TRANSIENT);
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    })
            .registerTypeHierarchyAdapter(Session.class, new SessionAdapter())
            ;


    public static Gson GSON = BUILDER.create();
    public static Gson GSON_PRETTY = BUILDER.setPrettyPrinting().create();

    public static Random RANDOM = new Random(System.currentTimeMillis());
    public static Random SECURE_RANDOM = new SecureRandom();

    public static JsonObject createFromError(Throwable t) {
        JsonObject object = new JsonObject();
        object.addProperty("error", t.getClass().getName());
        object.addProperty("message", t.getMessage());

        JsonArray stackTrace = new JsonArray(t.getStackTrace().length);
        for (StackTraceElement stackTraceElement : t.getStackTrace()) {
            stackTrace.add(stackTraceElement.toString());
        }
        object.add("stacktrace", stackTrace);
        return object;
    }
}