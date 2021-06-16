package me.imspooks.nettympa.backend.app.request;

import java.util.Map;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public interface Request {

    /**
     * Get an object with given key
     *
     * @param key Target key
     * @return Object bound to key
     */
    Object get(String key);

    /**
     * Merge the current request with another request
     *
     * @param other Target request
     * @param overwrite {@code true} to overwrite the current request variables if they also exist in the request to be merged with, {@code false} otherwise
     * @return Merged request
     */
    Request merge(Request other, boolean overwrite);

    /**
     * @return All stored values in the request
     */
    Map<String, Object> getValues();
}