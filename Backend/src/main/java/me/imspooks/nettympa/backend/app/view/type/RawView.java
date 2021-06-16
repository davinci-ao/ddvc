package me.imspooks.nettympa.backend.app.view.type;

import lombok.Getter;
import me.imspooks.nettympa.backend.app.section.Section;
import me.imspooks.nettympa.backend.app.session.Session;
import me.imspooks.nettympa.backend.app.view.View;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
public abstract class RawView implements View {

    @Getter private final Session session;

    protected RawView(Session session) {
        this.session = session;
    }

    public void generate(Section section) {

    }
}