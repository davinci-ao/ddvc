package me.imspooks.nettympa;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.imspooks.nettympa.backend.MpaServer;
import me.imspooks.nettympa.backend.app.session.SessionManager;
import me.imspooks.nettympa.backend.route.RouteManager;
import me.imspooks.nettympa.backend.settings.Environment;
import me.imspooks.nettympa.controller.AuthenticationController;
import me.imspooks.nettympa.controller.TestController;
import me.imspooks.nettympa.users.User;
import me.imspooks.nettympa.users.UserDatabase;
import org.apache.commons.cli.ParseException;

import java.util.Collections;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public class Server {

    @Getter private MongoClient mongoClient;
    @Getter private MongoDatabase internalDatabase;
    @Getter private UserDatabase userDatabase;
    @Getter private MpaServer mpaServer;

    public void registerRoutes() {
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
    }

    public void setupDatabase() {
        Environment environment = this.mpaServer.getEnvironment();
        this.mongoClient = new MongoClient(new ServerAddress(
                environment.getDatabaseSettings().getHost(),
                environment.getDatabaseSettings().getPort()
        ),
                Collections.singletonList(MongoCredential.createCredential(
                        environment.getDatabaseSettings().getUsername(),
                        environment.getDatabaseSettings().getDatabase(),
                        environment.getDatabaseSettings().getPassword().toCharArray()))
        );
        this.internalDatabase = this.mongoClient.getDatabase(environment.getDatabaseSettings().getDatabase());

        System.out.println("environment = " + environment);

        (this.userDatabase = new UserDatabase(this.internalDatabase)).createIndex();

        this.userDatabase.createUser(new User("nickversluis446@gmail.com", "ImSpooks"), "password");
        this.userDatabase.createUser(new User("jangarretsen01@gmail.com", "Yumpi"), "password");
    }

    public Server(String... args) {
        this.registerRoutes();

        try {
            SessionManager.setEnabled(true);

            this.mpaServer = new MpaServer(args);
        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }

        this.setupDatabase();
    }

    public static void main(String[] args) {
        new Server(args);
    }
}