package me.imspooks.nettympa.backend.server;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import me.imspooks.nettympa.backend.Mpa;
import me.imspooks.nettympa.backend.app.request.JsonRequest;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.request.reader.RequestReaderType;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;
import me.imspooks.nettympa.backend.app.response.type.RawResponse;
import me.imspooks.nettympa.backend.app.response.type.RedirectResponse;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.session.SessionManager;
import me.imspooks.nettympa.backend.files.FileManager;
import me.imspooks.nettympa.backend.global.Global;
import me.imspooks.nettympa.backend.route.RouteExecutor;
import me.imspooks.nettympa.backend.route.RouteManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Mpa mpa;

    public ServerHandler(Mpa mpa) {
        this.mpa = mpa;
    }

    public ServerHandler(boolean autoRelease, Mpa mpa) {
        super(autoRelease);
        this.mpa = mpa;
    }

    public ServerHandler(Class<? extends FullHttpRequest> inboundMessageType, Mpa mpa) {
        super(inboundMessageType);
        this.mpa = mpa;
    }

    public ServerHandler(Class<? extends FullHttpRequest> inboundMessageType, boolean autoRelease, Mpa mpa) {
        super(inboundMessageType, autoRelease);
        this.mpa = mpa;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String uri = msg.uri();
        if (uri.startsWith("/")) uri = uri.substring(1);


        Session session;

        // Handles the request
        RouteExecutor destination = RouteManager.getRoute(msg.method(), uri);

        Response response;
        if (destination.getDestination() == null) {
            // No route is set for this request, search for files in the public folder
            Path path = mpa.getRoot().resolve("public").resolve(uri.replace("%20", " "));

            // Convert the file to a htt response and send it back to the client
            ctx.writeAndFlush(this.createResponse(FileManager.toResponse(path)));
            return;
        } else {

            // Get session
            if (SessionManager.isEnabled()) {
                if (msg.headers().contains(HttpHeaderNames.COOKIE)) {
                    session = mpa.getSessionManager().getSessionFromCookies(mpa, ServerCookieDecoder.STRICT.decode(msg.headers().get(HttpHeaderNames.COOKIE)));
                } else {
                    session = mpa.getSessionManager().generateSession();
                }
            } else {
                session = Session.EMPTY_SESSION;
            }

            // Execute destination
            response = destination.execute(this.getRequest(msg), session);
        }

        FullHttpResponse httpResponse;
        if (response instanceof RedirectResponse) {
            // Redirect response
            httpResponse = this.createRedirect((RedirectResponse) response);
            ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        // Converts the result into a byte array
        httpResponse = this.createResponse(response);


        // Send a cookie to the client with the session key
        if (SessionManager.isEnabled()) {
            if (session != null) {
                if (session.isNew()) {
                    Cookie cookie = new DefaultCookie("SESSION_ID", session.getKey());

                    if (!msg.headers().get(HttpHeaderNames.HOST).equalsIgnoreCase("localhost"))
                        cookie.setDomain(msg.headers().get(HttpHeaderNames.HOST));
                    cookie.setPath("/");

                    httpResponse.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
                    session.setUsed();
                }
                session.refresh();

                try {
                    mpa.getSessionManager().saveToFile(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ctx.writeAndFlush(httpResponse);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.writeAndFlush(this.createResponse(new RawResponse(ResponseType.JSON, Global.GSON.toJson(Global.createFromError(cause)).getBytes(StandardCharsets.UTF_8))));
//        System.err.println(cause.getClass().getName() + ": " + cause.getMessage());
    }

    /**
     * Creates a http response
     *
     * @param response response instance
     * @return FullHttpResponse instance
     */
    private FullHttpResponse createResponse(Response response) {
        ByteBuf content = Unpooled.wrappedBuffer(response.getData());
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, response.getResponseType().getContentType());
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return httpResponse;
    }

    /**
     * Creates a http response
     *
     * @param response response instance
     * @return FullHttpResponse instance
     */
    private FullHttpResponse createRedirect(RedirectResponse response) {
        ByteBuf content = Unpooled.wrappedBuffer(response.getData());
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND, content);
        httpResponse.headers().set(HttpHeaderNames.LOCATION, response.getRedirect());
        return httpResponse;
    }

    private Request getRequest(FullHttpRequest msg) {
        JsonObject object = new JsonObject();

        try {
            if (msg.content().readableBytes() > 0) {
                byte[] buffer = new byte[msg.content().readableBytes()];
                msg.content().readBytes(buffer);

                return RequestReaderType.getFromContentType(msg.headers().get(HttpHeaderNames.CONTENT_TYPE)).getRequestReader().read(buffer);
            }
        } catch (Exception e) {
            System.err.println(Global.GSON.toJson(object));
            e.printStackTrace();
        }

        return new JsonRequest(object);
    }
}