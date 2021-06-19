package me.imspooks.nettympa.controller;

import me.imspooks.nettympa.backend.app.middleware.Middleware;
import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.request.validator.ValidationField;
import me.imspooks.nettympa.backend.app.request.validator.Validator;
import me.imspooks.nettympa.backend.app.request.validator.error.ValidationCollection;
import me.imspooks.nettympa.backend.app.request.validator.types.ValidationMinimumSize;
import me.imspooks.nettympa.backend.app.response.Response;
import me.imspooks.nettympa.backend.app.section.type.FileSection;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.files.FileManager;

import java.io.IOException;

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

    public Response validationTest(Session session, ValidationCollection previousErrors) throws IOException {
        System.out.println("previousErrors = " + previousErrors);

        FileSection section = new FileSection("content", FileManager.getFromView("section/test/validation-test.html"));
        if (previousErrors != null && previousErrors.size() > 0) {
            section.setValidationErrors(previousErrors);
        }

        return this.getPage(session, section);
    }

    public Response validationTestExecute(Request request, Session session) throws IOException {
        System.out.println("request = " + request.getValues());

        Validator validator = new Validator(
                new ValidationField("exampleFormControlTextarea1", new ValidationMinimumSize(25))
        );

        ValidationCollection errorCollection = validator.runValidation(request);

        System.out.println("errorCollection = " + errorCollection);

        if (errorCollection.hasError()) {
            return this.validationTest(session, errorCollection);
        }

        return this.getPage(session);
    }

}