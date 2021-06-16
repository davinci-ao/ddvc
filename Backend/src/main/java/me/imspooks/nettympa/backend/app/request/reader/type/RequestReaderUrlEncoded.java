package me.imspooks.nettympa.backend.app.request.reader.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.imspooks.nettympa.backend.app.request.JsonRequest;
import me.imspooks.nettympa.backend.app.request.Request;

/**
 * Created by Nick on 10 jul. 2020.
 * Copyright © ImSpooks
 */
public class RequestReaderUrlEncoded implements RequestReader {

    @Override
    public Request read(byte[] buffer) {
        String json = new String(buffer);
        JsonObject object;

        try { // Raw json data
            object = JsonParser.parseString(json).getAsJsonObject();

        } catch (Exception e) { // key1=val1&key2=val2
            object = new JsonObject();

            for (String s : json.split("&")) {
                String[] keyVal = s.split("=");

                object.addProperty(keyVal[0], keyVal[1].replace("+", " ").replace("%20", " "));
            }
        }
        return new JsonRequest(object);
    }
}