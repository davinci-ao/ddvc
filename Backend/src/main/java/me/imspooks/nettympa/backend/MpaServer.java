package me.imspooks.nettympa.backend;

import lombok.Getter;
import me.imspooks.nettympa.backend.app.session.SessionManager;
import me.imspooks.nettympa.backend.files.FileManager;
import me.imspooks.nettympa.backend.server.AppServer;
import me.imspooks.nettympa.backend.server.AppServerImpl;
import me.imspooks.nettympa.backend.settings.Environment;
import me.imspooks.nettympa.backend.settings.MPASettings;
import me.imspooks.nettympa.backend.settings.Settings;
import me.imspooks.nettympa.backend.util.CliUtil;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public class MpaServer extends Mpa {

    @Getter private AppServer appServer;
    @Getter private SessionManager sessionManager;
    @Getter private MPASettings settings;
    @Getter private final Environment environment;
    @Getter private final Path root;

    public MpaServer(String... args) throws InterruptedException, ParseException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            appServer.shutdown();
            System.out.println("Shut down.");
        }));

        this.root = Paths.get("");

        CliUtil cli = new CliUtil();
        Consumer<CliUtil> configuration = this.loadConfiguration(cli);
        cli.parse(args);
        configuration.accept(cli);

        FileManager.setInstance(this);

        this.environment = Environment.loadEnvironment(this.root);

        (this.appServer = new AppServerImpl()).run(this);

        try {
            this.sessionManager = new SessionManager(this);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            return;
        }

        Mpa.setInstance(this);
    }

    private Consumer<CliUtil> loadConfiguration(CliUtil cli) {
        // Load configuration
        Option configPath = Option.builder("cf").longOpt("config-file").hasArg().desc("Path to config").build();
        cli.addOption(configPath);

        return util -> {
            String path = "configuration.json";
            if (util.hasOption(configPath)) {
                path = util.get(configPath);
            }

            Path targetPath = Paths.get(path);
            try {
                this.settings = Settings.load(targetPath, MPASettings.class);
            } catch (Exception e) {
                this.settings = new MPASettings();

                try {
                    Settings.save(targetPath, settings);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    @Override
    public Path getWorkPath() {
        return this.root.resolve("work");
    }

    public static void main(String[] args) {
        try {
            new MpaServer(args);
        } catch (InterruptedException | ParseException e) {
            e.printStackTrace();
        }
    }
}