package me.imspooks.nettympa.backend.app.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 08 jul. 2020.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
@Getter
public class JsonRequest implements Request {

    private final JsonObject object;

    @Override
    public JsonElement get(String key) {
        return this.object.get(key);
    }

    @Override
    public Request merge(Request other, boolean overwrite) {
        for (Map.Entry<String, ?> value : other.getValues().entrySet()) {
            object.add(value.getKey(), JsonParser.parseString(value.getValue().toString()));
        }
        return null;
    }

    @Override
    public Map<String, Object> getValues() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : this.object.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Casts the request to a json request
     *
     * @param request Target request
     * @return Converted request
     */
    public static JsonRequest fromRequest(Request request) throws ClassCastException {
        if (request instanceof JsonRequest) {
            return (JsonRequest) request;
        }

        // convert other requests if they exist


        // Throw an exception if it can not be converted
        throw new ClassCastException("Unable to convert the request to a json request");
    }
}