package me.imspooks.nettympa.backend.app.request.reader.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.imspooks.nettympa.backend.app.request.JsonRequest;
import me.imspooks.nettympa.backend.app.request.Request;

/**
 * Created by Nick on 10 jul. 2020.
 * Copyright © ImSpooks
 */
public class RequestReaderPlain implements RequestReader {

    @Override
    public Request read(byte[] data) {
        String json = new String(data);
        JsonObject object;

        json = json.replace("\r", "\n").replace("\n\n", "\n");

        try { // Raw json data
            object = JsonParser.parseString(json).getAsJsonObject();

        } catch (Exception e) { // key1=val1&key2=val2
            object = new JsonObject();

            for (String s : json.split("\n")) {
                String[] keyVal = s.split("=");

                String key = keyVal[0];
                String value = keyVal[1].replace("+", " ").replace("%20", " ");

                if (object.has(key)) {
                    JsonArray array;
                    if (object.get(key).isJsonArray()) {
                        array = object.get(key).getAsJsonArray();
                    } else {
                        (array = new JsonArray()).add(object.get(key));
                    }
                    array.add(value);

                    object.add(key, array);
                } else {
                    object.addProperty(key, value);
                }
            }
        }
        return new JsonRequest(object);
    }
}