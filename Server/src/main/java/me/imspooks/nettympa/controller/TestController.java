package me.imspooks.nettympa.controller;

import me.imspooks.nettympa.backend.app.middleware.Middleware;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.request.validator.ValidationError;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.response.ResponseType;
import me.imspooks.nettympa.backend.app.response.type.RawResponse;
import me.imspooks.nettympa.backend.app.response.type.RedirectResponse;
import me.imspooks.nettympa.backend.app.section.type.FileSection;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.files.FileManager;
import me.imspooks.nettympa.backend.global.Global;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public class TestController extends BaseController {

    public TestController() {
        this.addMiddleware(new Middleware("test", (request, session) -> {
            if (!session.containsKey("user")) {
//                return new RedirectResponse("/login");
            }
            return null;
        }));
    }

    public Response test(Session session) throws IOException {
        return this.getPage(session);
    }

    public Response validationTest(Request request, Session session, Map<String, ValidationError> previousErrors) throws IOException {
        System.out.println("previousErrors = " + previousErrors);

        FileSection section = new FileSection("content", FileManager.getFromView("section/test/validation-test.html"));
        if (previousErrors != null && previousErrors.size() > 0) {
            section.setValidationErrors(previousErrors);
        }

        return this.getPage(session, section);
    }

    public Response validationTestExecute(Request request, Session session) throws IOException {

        return this.getPage(session);
    }

}