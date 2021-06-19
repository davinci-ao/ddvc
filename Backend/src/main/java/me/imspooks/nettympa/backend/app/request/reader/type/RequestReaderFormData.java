package me.imspooks.nettympa.backend.app.request.reader.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.nettympa.backend.app.request.JsonRequest;
import me.imspooks.nettympa.backend.app.request.Request;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 10 jul. 2020.
 * Copyright © ImSpooks
 */
public class RequestReaderFormData implements RequestReader {

    @Override
    public Request read(byte[] data) {
        String input = new String(data);
        Pattern splitter = Pattern.compile("-{27,29}\\d+");

        JsonObject object = new JsonObject();

        // TODO
        for (String s : splitter.split(input)) {
            String[] splitted = s.split(System.lineSeparator());
            if (splitted[0].isEmpty())
                splitted = (String[]) ArrayUtils.subarray(splitted, 1, splitted.length);

            Pattern pattern = Pattern.compile("Content-Disposition: form-data; name=\"(.*?)\"");
            Matcher matcher = pattern.matcher(s);

            while (matcher.find()) {
                Pattern fileNamePattern = Pattern.compile("filename=\"(.*?)\"");
                Matcher fileNameMatcher = fileNamePattern.matcher(s);

                String fileName = null;

                while (fileNameMatcher.find()) {
                    fileName = fileNameMatcher.group(1);
                }

                JsonElement response;
                if (fileName != null){
                    // its a file
                    response = ContentType.FILE.getFunction().apply(splitted);
                } else {
                    // its not a file
                    response = ContentType.TEXT.getFunction().apply(splitted);
                }

                String key = matcher.group(1);

                if (object.has(key)) {
                    JsonArray array;
                    if (object.get(key).isJsonArray()) {
                        array = object.get(key).getAsJsonArray();
                    } else {
                        (array = new JsonArray()).add(object.get(key));
                    }
                    array.add(response);

                    object.add(key, array);
                } else {
                    object.add(key, response);
                }
            }
        }
        return new JsonRequest(object);
    }

    @RequiredArgsConstructor
    @Getter
    private enum ContentType {
        TEXT("text", input -> {
            return new JsonPrimitive(StringUtils.join(ArrayUtils.subarray(input, 2, input.length), System.lineSeparator()));
        }),
        FILE("file", input -> {
            JsonObject object = new JsonObject();
            object.addProperty("type", input[1].substring("Content-Type: ".length()));
            object.addProperty("content", StringUtils.join(ArrayUtils.subarray(input, 3, input.length), System.lineSeparator()));
            return object;
        }),
        ;

        private final String name;
        private final Function<String[], JsonElement> function;
    }
}