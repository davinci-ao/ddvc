package me.imspooks.nettympa.backend.app.session.impl;

import lombok.Getter;
import me.imspooks.nettympa.backend.app.session.Session;

import java.util.HashMap;

/**
 * Created by Nick on 29 Jun 2020.
 * Copyright © ImSpooks
 */
public class EmptySession extends HashMap<String, Object> implements Session {

    @Getter private final String key;

    public EmptySession(String key) {
        this.key = key;
    }


    @Override
    public boolean hasExpired() {
        return false;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void setLastUsed(long time) {

    }

    @Override
    public long getLastUsed() {
        return 0;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public void setUsed() {

    }

    @Override
    public boolean canSave() {
        return false;
    }
}