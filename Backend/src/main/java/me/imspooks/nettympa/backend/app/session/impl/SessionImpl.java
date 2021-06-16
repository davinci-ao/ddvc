package me.imspooks.nettympa.backend.app.session.impl;

import lombok.Getter;
import lombok.Setter;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.session.SessionManager;

import java.util.HashMap;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public class SessionImpl extends HashMap<String, Object> implements Session {

    private final String key;

    public SessionImpl(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Getter @Setter private long lastUsed = 0;

    @Override
    public boolean hasExpired() {
        return System.currentTimeMillis() - lastUsed > SessionManager.getTimeout();
    }

    @Override
    public void refresh() {
        this.setLastUsed(System.currentTimeMillis());
    }

    private boolean used = false;

    @Override
    public boolean isNew() {
        return !this.used;
    }

    @Override
    public void setUsed() {
        used = true;
    }
}