package me.imspooks.nettympa;

import lombok.Getter;
import me.imspooks.nettympa.backend.MpaServer;
import me.imspooks.nettympa.backend.app.session.SessionManager;
import me.imspooks.nettympa.backend.route.RouteManager;
import me.imspooks.nettympa.controller.AuthenticationController;
import me.imspooks.nettympa.controller.TestController;
import me.imspooks.nettympa.users.User;
import org.apache.commons.cli.ParseException;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public class Server {

    @Getter private MpaServer mpaServer;

    public Server(String... args) {
        // Login route
        RouteManager.get("validation-test", TestController.class, "validationTest");
        RouteManager.post("validation-test", TestController.class, "validationTestExecute");

        RouteManager.get("login", AuthenticationController.class, "showLogin");
        RouteManager.post("login", AuthenticationController.class, "login");
        // Register route
        RouteManager.get("register", AuthenticationController.class, "showRegister");
        RouteManager.post("register", AuthenticationController.class, "login");

        RouteManager.get("", TestController.class, "test");
        RouteManager.get("store", TestController.class, "store");
        RouteManager.get("get", TestController.class, "get");
        RouteManager.get("redirect", TestController.class, "redirect");
        RouteManager.get("user/{username}", TestController.class, "wildcardTest");

        try {
            SessionManager.setEnabled(true);

            this.mpaServer = new MpaServer(args);
        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(args);
    }
}