package me.imspooks.nettympa.controller;

import me.imspooks.nettympa.backend.app.controller.Controller;
import me.imspooks.nettympa.backend.app.layout.Layout;
import me.imspooks.nettympa.backend.app.response.type.ViewResponse;
import me.imspooks.nettympa.backend.app.section.component.Component;
import me.imspooks.nettympa.backend.app.section.type.FileSection;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.view.type.SectionedView;
import me.imspooks.nettympa.backend.files.FileManager;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Nick on 20 Jun 2021.
 * Copyright © ImSpooks
 */
public class BaseController extends Controller {

    public final ViewResponse getPage(Session session, FileSection... sections) throws IOException {
        Layout layout = () -> FileManager.getFromView("layout/layout.html");

        SectionedView view = new SectionedView(layout);

        // Header
        FileSection header = new FileSection("header", FileManager.getFromView("section/template/header.html"));
        header.addComponent("nav.bar", new Component("nav.bar", FileManager.getFromView("component/nav.bar." + (!session.containsKey("user") ? "auth" : "guest") + ".html")));
        view.registerSection(header);

        // Footer
        FileSection footer = new FileSection("footer", FileManager.getFromView("section/template/footer.html"));
        view.registerSection(footer);

        for (FileSection section : sections) {
            view.registerSection(section);
        }

        return new ViewResponse(view, session);
    }
}