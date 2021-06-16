package me.imspooks.nettympa.backend.app.response;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public interface Response {

    /**
     * @return Reponse type
     */
    ResponseType getResponseType();

    /**
     * @return Reponse data
     */
    byte[] getData();
}