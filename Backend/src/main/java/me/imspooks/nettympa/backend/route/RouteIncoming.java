package me.imspooks.nettympa.backend.route;

import io.netty.handler.codec.http.HttpMethod;

import java.util.Objects;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public class RouteIncoming {

    private final HttpMethod method;
    private final String uri;

    public RouteIncoming(HttpMethod method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    /**
     * @return Http method
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * @return Uri for routing
     */
    public String getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteIncoming)) return false;
        RouteIncoming route = (RouteIncoming) o;
        return Objects.equals(method, route.method) &&
                Objects.equals(uri, route.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uri);
    }
}