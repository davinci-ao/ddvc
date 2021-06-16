package me.imspooks.nettympa.backend.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import me.imspooks.nettympa.backend.Mpa;
import me.imspooks.nettympa.backend.logger.MpaLogger;


public class AppServerImpl implements AppServer {

    @Getter private EventLoopGroup bossGroup, workerGroup;

    public void run(Mpa mpa) throws InterruptedException {
        this.bossGroup = new NioEventLoopGroup(8);
        this.workerGroup = new NioEventLoopGroup(8);

//        try {
            ServerBootstrap httpBootstrap = new ServerBootstrap();

            httpBootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer(mpa))
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture httpChannel = httpBootstrap.bind(mpa.getSettings().getPort()).sync();

        MpaLogger.getLogger().info("Started MPA application on port {}", mpa.getSettings().getPort());
/*            // Wait until the server socket is closed
            httpChannel.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }*/
    }

    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}