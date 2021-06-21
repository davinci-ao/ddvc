package me.imspooks.nettympa.users;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nick on 16 jun. 2021.
 * Copyright © ImSpooks
 */
@Data
public class User {

    private final String email;
    private final String username;
    private transient long timeoutAfter = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);

    public void update() {
        this.timeoutAfter = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
    }
}