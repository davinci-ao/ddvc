package me.imspooks.nettympa.backend.app.request.reader.type;

import me.imspooks.nettympa.backend.app.request.Request;

/**
 * Created by Nick on 10 jul. 2020.
 * Copyright © ImSpooks
 */
public interface RequestReader {

    /**
     * Read the incoming data and returns a request object
     *
     * @param data Incoming data
     * @return Request object
     */
    Request read(byte[] data);
}