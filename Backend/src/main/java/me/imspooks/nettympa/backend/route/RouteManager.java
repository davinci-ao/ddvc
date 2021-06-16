package me.imspooks.nettympa.backend.route;

import io.netty.handler.codec.http.HttpMethod;
import me.imspooks.nettympa.backend.app.controller.Controller;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
public class RouteManager {

    private static boolean CASE_SENSITIVE;

    public static void setCaseSensitive(boolean caseSensitive) {
        CASE_SENSITIVE = caseSensitive;
    }

    public static boolean isCaseSensitive(boolean caseSensitive) {
        return caseSensitive;
    }

    private static final Map<RouteIncoming, RouteDestination> ROUTES = new HashMap<>();

    /**
     * Register a route
     *
     * @param incoming Incoming route
     * @param destination Destination
     */
    public static void register(RouteIncoming incoming, RouteDestination destination) {
        ROUTES.put(incoming, destination);
    }

    /**
     * Register a route
     *
     * @param httpMethod Http Method
     * @param uri Target uri
     * @param controller Controller class
     * @param method Controller method
     * @see RouteManager#register(RouteIncoming, RouteDestination)
     */
    public static void register(HttpMethod httpMethod, String uri, Class<? extends Controller> controller, String method) {
        register(new RouteIncoming(httpMethod, uri), new RouteDestination(controller, method));
    }

    /**
     * Register a get route
     *
     * @param uri Target uri
     * @param destination Destination
     * @see RouteManager#register(RouteIncoming, RouteDestination)
     */
    public static void get(String uri, RouteDestination destination) {
        ROUTES.put(new RouteIncoming(HttpMethod.GET, uri), destination);
    }

    /**
     * Register a get route
     *
     * @param uri Target uri
     * @param controller Controller class
     * @param method Controller method
     * @see RouteManager#get(String, RouteDestination)
     */
    public static void get(String uri, Class<? extends Controller> controller, String method) {
        get(uri, new RouteDestination(controller, method));
    }

    /**
     * Register a post route
     *
     * @param uri Target uri
     * @param destination Destination
     * @see RouteManager#register(RouteIncoming, RouteDestination)
     */
    public static void post(String uri, RouteDestination destination) {
        ROUTES.put(new RouteIncoming(HttpMethod.POST, uri), destination);
    }

    /**
     * Register a post route
     *
     * @param uri Target uri
     * @param controller Controller class
     * @param method Controller method
     * @see RouteManager#post(String, RouteDestination)
     */
    public static void post(String uri, Class<? extends Controller> controller, String method) {
        post(uri, new RouteDestination(controller, method));
    }

    /**
     * Register a put route
     *
     * @param uri Target uri
     * @param destination Destination
     * @see RouteManager#register(RouteIncoming, RouteDestination)
     */
    public static void put(String uri, RouteDestination destination) {
        ROUTES.put(new RouteIncoming(HttpMethod.PUT, uri), destination);
    }

    /**
     * Register a put route
     *
     * @param uri Target uri
     * @param controller Controller class
     * @param method Controller method
     * @see RouteManager#put(String, RouteDestination)
     */
    public static void put(String uri, Class<? extends Controller> controller, String method) {
        put(uri, new RouteDestination(controller, method));
    }

    /**
     * Register a delete route
     *
     * @param uri Target uri
     * @param destination Destination
     * @see RouteManager#register(RouteIncoming, RouteDestination)
     */
    public static void delete(String uri, RouteDestination destination) {
        ROUTES.put(new RouteIncoming(HttpMethod.DELETE, uri), destination);
    }

    /**
     * Register a delete route
     *
     * @param uri Target uri
     * @param controller Controller class
     * @param method Controller method
     * @see RouteManager#delete(String, RouteDestination)
     */
    public static void delete(String uri, Class<? extends Controller> controller, String method) {
        delete(uri, new RouteDestination(controller, method));
    }

    /**
     * Register a patch route
     *
     * @param uri Target uri
     * @param destination Destination
     * @see RouteManager#register(RouteIncoming, RouteDestination)
     */
    public static void patch(String uri, RouteDestination destination) {
        ROUTES.put(new RouteIncoming(HttpMethod.PATCH, uri), destination);
    }

    /**
     * Register a patch route
     *
     * @param uri Target uri
     * @param controller Controller class
     * @param method Controller method
     * @see RouteManager#patch(String, RouteDestination)
     */
    public static void patch(String uri, Class<? extends Controller> controller, String method) {
        patch(uri, new RouteDestination(controller, method));
    }

    /**
     * Returns a map with all the different routes that are registered
     *
     * @return All registered routes
     */
    public static Map<RouteIncoming, RouteDestination> getRoutes() {
        return ROUTES;
    }

    /**
     * Get a destination with given route
     * 
     * @param incoming Incoming request
     * @return Destination
     */
    public static RouteExecutor getRoute(RouteIncoming incoming) {
        return new RouteExecutor(ROUTES.get(incoming), Collections.emptyMap());
    }

    /**
     * Get a destination with given route
     *
     * @see RouteManager#getRoute(RouteIncoming)
     * @param method Http Method
     * @param uri Target uri
     * @return Destination
     */
    public static RouteExecutor getRoute(HttpMethod method, String uri) {
        for (RouteIncoming key : ROUTES.keySet()) {
            if (method != key.getMethod())
                continue;

            if (key.getUri().split("/").length != uri.split("/").length)
                continue;

            String url = key.getUri();

            // Check for possible wild cards
            List<String> wildcardNames = new ArrayList<>();
            Pattern pattern = Pattern.compile("\\{.*}", Pattern.MULTILINE);

            for (String s : url.split("/")) {
                Matcher matcher = pattern.matcher(s);

                while (matcher.find()) {
                    String group = matcher.group(0);
                    url = url.replace(group, "*");
                    wildcardNames.add(group.substring(1, group.length() - 1));
                }
            }

            Map<String, String> wildcards = new LinkedHashMap<>();

            StringBuilder finalUrl = new StringBuilder();
            
            String[] urlSplit = url.split("/");
            String[] uriSplit = uri.split("/");
            for (int i = 0; i < urlSplit.length; i++) {
                String wildUrl = urlSplit[i];
                if (wildUrl.equalsIgnoreCase("*")) {
                    wildcards.put(wildcardNames.get(wildcards.size()), uriSplit[i]);
                    finalUrl.append(uriSplit[i]);
                } else {
                    finalUrl.append(wildUrl);
                }
                
                if (i < urlSplit.length - 1)
                    finalUrl.append("/");
            }

            if ((CASE_SENSITIVE && finalUrl.toString().equals(uri)) || (!CASE_SENSITIVE && finalUrl.toString().equalsIgnoreCase(uri))) {
                return new RouteExecutor(ROUTES.get(key), wildcards);
            }
        }
        return getRoute(new RouteIncoming(method, uri));
    }
    
    
}