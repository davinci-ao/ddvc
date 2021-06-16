package me.imspooks.nettympa.backend.app.middleware;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.session.Session;

import java.util.function.BiFunction;

/**
 * Created by Nick on 16 jun. 2021.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public class Middleware {

    @Getter private final String method;
    @Getter private final BiFunction<Request, Session, Response> check;

    public Response check(Request request, Session session) {
        return this.check.apply(request, session);
    }
}