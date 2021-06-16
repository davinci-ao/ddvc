package me.imspooks.nettympa.backend.app.view;


import me.imspooks.nettympa.backend.app.request.Request;
import me.imspooks.nettympa.backend.app.session.Session;

import java.io.IOException;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public interface View {

    //TODO documentation

    void generate(Session session);
    byte[] parse() throws IOException;
}