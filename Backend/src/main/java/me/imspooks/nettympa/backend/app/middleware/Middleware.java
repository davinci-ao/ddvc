package me.imspooks.nettympa.backend.app.middleware;

import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.session.Session;

import java.util.function.BiFunction;

/**
 * Created by Nick on 16 jun. 2021.
 * Copyright © ImSpooks
 */
// TODO documentation
public class Middleware {

    private final String method;
    private final BiFunction<Request, Session, Response> check;

    public Middleware(String method, BiFunction<Request, Session, Response> check) {
        this.method = method;
        this.check = check;
    }

    public Middleware(BiFunction<Request, Session, Response> check) {
        this("", null);
    }



    public Response check(Request request, Session session) {
        return this.check.apply(request, session);
    }

    public String getMethod() {
        return method;
    }
}