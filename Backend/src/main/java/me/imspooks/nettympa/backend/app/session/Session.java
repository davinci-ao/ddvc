package me.imspooks.nettympa.backend.app.session;

import me.imspooks.nettympa.backend.app.session.impl.EmptySession;

import java.util.Map;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public interface Session extends Map<String, Object> {

    Session EMPTY_SESSION = new EmptySession("empty_session");

    /**
     * @return Session key
     */
    String getKey();

    /**
     * @return {@code true} when the session has expired, {@code false} otherwise
     */
    boolean hasExpired();

    /**
     * Refresh the session time
     */
    void refresh();


    /**
     * Set the time in millis when the session was last used
     * Used for deserializing
     *
     * @param time Last usage time
     */
    void setLastUsed(long time);

    /**
     * @return Time in millis when the session was last saved
     */
    long getLastUsed();

    /**
     * @return If the session is newly created
     */
    boolean isNew();

    /**
     * Set the session status to used
     */
    void setUsed();

    /**
     * @return Whether the session can be saved to a file
     */
    default boolean canSave() {
        return true;
    }
}