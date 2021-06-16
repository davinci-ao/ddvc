package me.imspooks.nettympa.backend.route;

import me.imspooks.nettympa.backend.app.controller.Controller;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public class RouteDestination {

    private final Class<? extends Controller> controllerClass;
    private final String method;

    public RouteDestination(Class<? extends Controller> controllerClass, String method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    private Controller controller;

    /**
     * @return Controller instance
     */
    public Controller getController() {
        if (this.controller == null) {
            try {
                this.controller = this.controllerClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return this.controller;
    }

    /**
     * @return Controller class
     */
    public Class<? extends Controller> getControllerClass() {
        return this.controllerClass;
    }

    /**
     * @return Method name
     */
    public String getMethod() {
        return this.method;
    }
}