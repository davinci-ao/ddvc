package me.imspooks.nettympa.backend.app.controller;

import lombok.Data;
import me.imspooks.nettympa.backend.app.middleware.Middleware;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */

public class Controller {

    private final List<Middleware> middlewareList = new ArrayList<>();

    public Response runMiddleware(String method, Request request, Session session) {
        for (Middleware middleware : middlewareList) {
            if (middleware.getMethod().equalsIgnoreCase(method)) {
                Response response = middleware.check(request, session);
                if (response != null) {
                    return response;
                }
            }
        }
        return null;
    }

    /**
     * Add middleware to the controller
     * @param middleware Middleware isntance
     */
    public void addMiddleware(Middleware middleware) {
        this.middlewareList.add(middleware);
    }
}