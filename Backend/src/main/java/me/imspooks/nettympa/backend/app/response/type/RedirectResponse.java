package me.imspooks.nettympa.backend.app.response.type;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.Data;
import lombok.Getter;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;

/**
 * Created by Nick on 16 jun. 2021.
 * Copyright © ImSpooks
 */
@Data
public class RedirectResponse implements Response {

    private final String redirect;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.PLAIN;
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}