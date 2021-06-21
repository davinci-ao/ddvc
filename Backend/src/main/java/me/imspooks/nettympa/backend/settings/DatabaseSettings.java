package me.imspooks.nettympa.backend.settings;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by Nick on 14 aug. 2020.
 * Copyright © ImSpooks
 */
@ToString
public class DatabaseSettings {

    @Getter private String username = "INSERT_USR_HERE";
    @Getter private String password = "INSERT_PWD_HERE";
    @Getter private String host = "INSERT_HOST_HERE";
    @Getter private int port = 27017;
    @Getter private String database = "INSERT_DB_HERE";
}