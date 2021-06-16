package me.imspooks.nettympa.backend.route;

import com.google.gson.reflect.TypeToken;
import me.imspooks.nettympa.backend.app.controller.Controller;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;
import me.imspooks.nettympa.backend.app.response.type.RawResponse;
import me.imspooks.nettympa.backend.app.response.type.ViewResponse;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.view.View;
import me.imspooks.nettympa.backend.global.Global;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class RouteExecutor {

    private final RouteDestination destination;
    private final Map<String, String> wildcards;

    public RouteExecutor(RouteDestination destination, Map<String, String> wildcards) {
        this.destination = destination;
        this.wildcards = wildcards;
    }

    /**
     * Execute the route and return a response
     * @return Incoming response
     */
    public Response execute(Request request, Session session) {
        Controller controller = this.destination.getController();
        if (controller == null) {
            return new RawResponse(ResponseType.JSON, Global.GSON.toJson(Global.createFromError(new AssertionError("Controller is null"))).getBytes());
        }

        Class<?> wildcardType = new TypeToken<Map<String, String>>(){}.getRawType();

        try {
            for (Method method : controller.getClass().getMethods()) {
                if (method.getName().equals(this.destination.getMethod())) {

                    Response middlewareResponse = controller.runMiddleware(this.destination.getMethod(), request, session);
                    if (middlewareResponse != null) {
                        return middlewareResponse;
                    }

                    List<Object> params = new ArrayList<>();
                    for (Class<?> parameterType : method.getParameterTypes()) {
                        if (parameterType == Request.class) params.add(request);
                        if (parameterType == Session.class) params.add(session);
                        if (parameterType == wildcardType) params.add(this.wildcards);
                    }

                    Object response = method.invoke(controller, params.toArray(new Object[0]));
                    if (response == null)
                        throw new NullPointerException("Response is null");

                    if (response instanceof Response)
                        return (Response) response;
                    else if (response instanceof View)
                        return new ViewResponse((View) response, session);
                }
            }
        } catch (IllegalAccessException | IOException | NullPointerException e) {
            return new RawResponse(ResponseType.JSON, Global.GSON.toJson(Global.createFromError(e)).getBytes());
        } catch(InvocationTargetException e) {
            return new RawResponse(ResponseType.JSON, Global.GSON.toJson(Global.createFromError(e.getCause())).getBytes());
        }
        return new RawResponse(ResponseType.JSON, Global.GSON.toJson(Global.createFromError(new AssertionError("Method not found"))).getBytes());
    }

    /**
     * @return Route destination
     */
    public RouteDestination getDestination() {
        return this.destination;
    }

    /**
     * @return Wildcards
     */
    public Map<String, String> getWildcards() {
        return this.wildcards;
    }
}