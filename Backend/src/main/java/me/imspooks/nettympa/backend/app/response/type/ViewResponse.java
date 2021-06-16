package me.imspooks.nettympa.backend.app.response.type;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Getter;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.view.View;

import java.io.IOException;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class ViewResponse implements Response {

    @Getter private final View view;
    @Getter private final byte[] data;

    public ViewResponse(View view, Session session) throws IOException {
        this.view = view;
        this.view.generate(session);

        this.data = view.parse();
    }

    @Override
    public ResponseType getResponseType() {
        return ResponseType.HTML;
    }
}