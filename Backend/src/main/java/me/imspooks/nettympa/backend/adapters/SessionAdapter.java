package me.imspooks.nettympa.backend.adapters;

import com.google.gson.*;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.session.impl.SessionImpl;
import me.imspooks.nettympa.backend.global.Global;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class SessionAdapter implements JsonSerializer<Session>, JsonDeserializer<Session> {

    @Override
    public JsonElement serialize(Session src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("class", src.getClass().getName());
        object.addProperty("key", src.getKey());
        object.addProperty("last", src.getLastUsed());

        JsonObject values = new JsonObject();
        for (Map.Entry<String, Object> entry : src.entrySet()) {
            JsonObject value = new JsonObject();

/*            // Can only save these types of data, need to find a better workaround for this later
            if (entry.getValue() instanceof String || entry.getValue() instanceof Number || entry.getValue() instanceof Boolean || entry.getValue() instanceof Character) {
                value.addProperty("class", entry.getValue().getClass().getName());
                value.add("data", Global.GSON.toJsonTree(entry.getValue()));
            }*/

            StoredDataType dataType = StoredDataType.getFromObject(entry.getValue().getClass());
            if (dataType != null) {
                value.addProperty("class", entry.getValue().getClass().getName());
                value.add("data", Global.GSON.toJsonTree(entry.getValue()));
            }

            values.add(entry.getKey(), value);
        }
        object.add("values", values);

        return object;
    }

    @Override
    public Session deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String key = object.get("key").getAsString();
        Map<String, Object> sessionValues = new HashMap<>();

        for (Map.Entry<String, JsonElement> values : object.get("values").getAsJsonObject().entrySet()) {
            String id = values.getKey();

            JsonObject value = values.getValue().getAsJsonObject();
            try {
                Class<?> clazz = Class.forName(value.get("class").getAsString());
                Object data = Global.GSON.fromJson(value.get("data"), clazz);

                sessionValues.put(id, data);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                sessionValues.put(id, Global.GSON.toJson(value.get("data")));
            }
        }


        Session session;

        try {
            session = (Session) Class.forName(object.get("class").getAsString()).getConstructor(String.class).newInstance(key);
        } catch (NullPointerException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            session = new SessionImpl(key);
            e.printStackTrace();
        }
        session.putAll(sessionValues);
        session.setLastUsed(object.get("last").getAsLong());
        return session;
    }
}