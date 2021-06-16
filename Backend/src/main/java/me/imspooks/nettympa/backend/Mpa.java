package me.imspooks.nettympa.backend;

import me.imspooks.nettympa.backend.app.session.SessionManager;
import me.imspooks.nettympa.backend.server.AppServer;
import me.imspooks.nettympa.backend.settings.Environment;
import me.imspooks.nettympa.backend.settings.MPASettings;

import java.nio.file.Path;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public abstract class Mpa {

    private static Mpa instance;

    /**
     * MPA instance the application is running on
     *
     * @return Main instance
     */
    public static Mpa getInstance() {
        return instance;
    }

    /**
     * Set a new mpa instance
     *
     * @param instance MPA instance
     */
    public static void setInstance(Mpa instance) {
        Mpa.instance = instance;
    }

    /**
     * @return Session manager instance
     */
    public abstract SessionManager getSessionManager();

    /**
     * @return Application settings
     */
    public abstract MPASettings getSettings();

    /**
     * @return Environment settings
     */
    public abstract Environment getEnvironment();

    /**
     * @see MPASettings#getPort()
     * @return Application port
     */
    public int getPort() {
        return this.getSettings().getPort();
    }

    /**
     * @return Netty server
     */
    public abstract AppServer getAppServer();

    /**
     * @return Root folder
     */
    public abstract Path getRoot();

    /**
     * @return Work folder
     */
    public abstract Path getWorkPath();
}