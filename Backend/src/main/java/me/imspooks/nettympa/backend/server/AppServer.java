package me.imspooks.nettympa.backend.server;

import io.netty.channel.EventLoopGroup;
import me.imspooks.nettympa.backend.Mpa;

/**
 * Created by Nick on 27 Jun 2020.
 * Copyright © ImSpooks
 */
public interface AppServer {
    // TODO documentation

    EventLoopGroup getBossGroup();
    EventLoopGroup getWorkerGroup();

    void run(Mpa mpa) throws InterruptedException;
    void shutdown();
}