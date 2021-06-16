package me.imspooks.nettympa.backend.app.response.type;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Getter;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public class RawResponse implements Response {

    @Getter private final ResponseType responseType;
    @Getter private final byte[] data;

    public RawResponse(ResponseType responseType, byte[] data) {
        this.responseType = responseType;
        this.data = data;
    }

    public RawResponse(ResponseType responseType, String data) {
        this.responseType = responseType;
        this.data = data.getBytes();
    }
}