package me.imspooks.nettympa.users;

import lombok.Data;
import lombok.Getter;
import me.imspooks.nettympa.backend.Mpa;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nick on 16 jun. 2021.
 * Copyright © ImSpooks
 */
@Data
public class UserStorage {

    private Map<Integer, User> users = new HashMap<>();

    @Getter(lazy = true) private static final UserStorage instance = new UserStorage();


    public UserStorage() {
        // Task that clears users from memory if the user hasnt been touched for atleast an hour
        Mpa.getInstance().getAppServer().getWorkerGroup().scheduleAtFixedRate(() ->
                users.entrySet().removeIf(user -> System.currentTimeMillis() > user.getValue().getTimeoutAfter()), 1, 1, TimeUnit.MINUTES);
    }
}