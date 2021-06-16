package me.imspooks.nettympa.controller;

import me.imspooks.nettympa.backend.app.controller.Controller;
import me.imspooks.nettympa.backend.app.layout.Layout;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;
import me.imspooks.nettympa.backend.app.response.type.RawResponse;
import me.imspooks.nettympa.backend.app.response.type.RedirectResponse;
import me.imspooks.nettympa.backend.app.response.type.ViewResponse;
import me.imspooks.nettympa.backend.app.section.type.FileSection;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.view.type.SectionedView;
import me.imspooks.nettympa.backend.files.FileManager;
import me.imspooks.nettympa.backend.global.Global;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class TestController extends Controller {

    public Response test(Session session) throws IOException {
        Layout layout = () -> FileManager.getFromView("layout/layout.html");

        SectionedView view = new SectionedView(layout);

        FileSection fileSection = new FileSection("header", FileManager.getFromView("section/header.html"));

        view.registerSection(fileSection);

        return new ViewResponse(view, session);
    }

    public Response store(Request request, Session session, Map<String, String> wildcard) {
        session.put("testVariable", Global.RANDOM.nextInt(100));
        return new RawResponse(ResponseType.JSON, Global.GSON.toJson(session));
    }

    public Response get(Request request, Session session, Map<String, String> wildcard) {
        return new RawResponse(ResponseType.JSON, Global.GSON.toJson(session));
    }

    public Response redirect(Request request, Session session, Map<String, String> wildcard) {
        return new RedirectResponse("https://www.google.com");
    }

}