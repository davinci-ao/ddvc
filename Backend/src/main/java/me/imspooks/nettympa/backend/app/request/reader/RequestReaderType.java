package me.imspooks.nettympa.backend.app.request.reader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.nettympa.backend.app.request.reader.type.RequestReader;
import me.imspooks.nettympa.backend.app.request.reader.type.RequestReaderFormData;
import me.imspooks.nettympa.backend.app.request.reader.type.RequestReaderUrlEncoded;

/**
 * Created by Nick on 10 jul. 2020.
 * Copyright © ImSpooks
 *
 * TODO documentation
 */
@RequiredArgsConstructor
@Getter
public enum RequestReaderType {
    PLAIN("text/plain", new RequestReaderUrlEncoded()),
    FORM_URLENCODED("application/x-www-form-urlencoded", new RequestReaderUrlEncoded()),
    FORM_DATA("multipart/form-data", new RequestReaderFormData())
    ;

    public static RequestReaderType[] CACHE = values();

    private final String contentType;
    private final RequestReader requestReader;

    public static RequestReaderType getFromContentType(String contentType) {
        for (RequestReaderType requestReaderType : CACHE) {
            if (requestReaderType.getContentType().equalsIgnoreCase(contentType.split(";")[0])) {
                return requestReaderType;
            }
        }
        return PLAIN;
    }
}