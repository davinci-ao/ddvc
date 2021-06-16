package me.imspooks.nettympa.backend.app.session;

import io.netty.handler.codec.http.cookie.Cookie;
import me.imspooks.nettympa.backend.Mpa;
import me.imspooks.nettympa.backend.app.session.impl.SessionImpl;
import me.imspooks.nettympa.backend.global.Global;
import me.imspooks.nettympa.backend.logger.MpaLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public class SessionManager {

    private static boolean ENABLED = false;
    static long TIMEOUT = TimeUnit.DAYS.toMillis(1);

    private Path directory;

    public SessionManager(Mpa mpa) throws IOException {
        mpa.getAppServer().getWorkerGroup().scheduleAtFixedRate(() -> {

            if (isEnabled()) {

                this.sessions.entrySet().removeIf(entry -> {
                    if (entry.getValue().hasExpired()) {
                        try {
                            this.deleteFile(entry.getValue());
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    return false;
                });
            }
        }, 10, 10, TimeUnit.MINUTES);

        this.directory = mpa.getWorkPath().resolve("sessions");
        if (!Files.exists(this.directory))
            Files.createDirectories(this.directory);

        Files.list(this.directory).forEach(file -> {
            if (file.getFileName().toString().endsWith(".json")) {
                try {
                    Session session = this.fromFile(file);
                    session.setUsed();
                    this.sessions.put(session.getKey(), session);
                } catch (IOException e) {
                    MpaLogger.getLogger().warn("Failed to load session with key \"{" + file.getFileName().toString().replace(".json", "") + "}\"");
                }
            }
        });
    }

    /**
     * Set the session expire time
     *
     * @param timeout Expire time
     */
    public static void setTimeout(long timeout) {
        SessionManager.TIMEOUT = timeout;
    }

    /**
     * @return Timeout in milliseconds
     */
    public static long getTimeout() {
        return TIMEOUT;
    }

    private final Map<String, Session> sessions = new HashMap<>();

    /**
     * @return Get all existing sessions in a map
     */
    public Map<String, Session> getSessions() {
        return sessions;
    }

    /**
     * Generate a new session
     *
     * @return New session
     */
    public Session generateSession() {
        String key = this.generateKey();
        Session session = new SessionImpl(key);
        this.sessions.put(key, session);
        return session;
    }

    /**
     * Get a session with a given key
     * Generates a new session when it does not exist or is expired
     *
     * @param key Session key
     * @return Session with given key
     */
    public Session getSession(String key) {
        return this.sessions.get(key);
    }

    /**
     * Enable or disable sessions
     *
     * @param enabled {@code true} for enabled sessions, {@code false} otherwise
     */
    public static void setEnabled(boolean enabled) {
        SessionManager.ENABLED = enabled;
    }

    /**
     * @return {@code true} if sessions are enabled, {@code false} otherwise
     */
    public static boolean isEnabled() {
        return ENABLED;
    }

    /**
     * Store the session as a file
     * If a restart is necessary, the sessions will not be lost
     *
     * @param session Session to save
     * @throws IOException When saving failed
     */
    public void saveToFile(Session session) throws IOException {
        if (!session.canSave())
            return;

        if (!Files.exists(directory))
            Files.createDirectories(directory);

        Path file = directory.resolve(session.getKey() + ".json");

        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        Files.write(file, Global.GSON_PRETTY.toJson(session).getBytes());
    }

    /**
     * Delete the session file
     *
     * @param session Session to delete
     * @throws IOException When saving failed
     */
    public void deleteFile(Session session) throws IOException {
        if (!Files.exists(directory))
            Files.createDirectories(directory);

        Path file = directory.resolve(session.getKey() + ".json");

        if (Files.exists(file)) {
            Files.delete(file);
        }
    }

    /**
     * Load a session from a file
     *
     * @param file File to load
     * @throws IOException When saving failed
     */
    public Session fromFile(Path file) throws IOException {
        if (!Files.exists(directory))
            Files.createDirectories(directory);

        if (!Files.exists(file)) {
            return new SessionImpl(this.generateKey());
        }
        return Global.GSON_PRETTY.fromJson(new String(Files.readAllBytes(file)), SessionImpl.class);
    }

    /**
     * @return Generate a session key
     */
    private String generateKey() {
        SecureRandom RANDOM = new SecureRandom();
        char[] HEX = "123456789ABCDEF".toCharArray();

        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            key.append(HEX[RANDOM.nextInt(HEX.length)]);
        }

        return key.toString();
    }

    public Session getSessionFromCookies(Mpa mpa, Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.name().equalsIgnoreCase("SESSION_ID")) {
                Session session = mpa.getSessionManager().getSession(cookie.value());
                if (session != null && !session.hasExpired()) {
                    session.refresh();
                    return session;
                }
            }
        }
        return mpa.getSessionManager().generateSession();
    }
}