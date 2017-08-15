package com.silverhetch.horae.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

public class SocketClient implements SocketDevice {
    private final ComputeUnit computeUnit;
    private final List<EventLoopGroup> eventLoops;
    private final String host;
    private final int port;
    private boolean running;

    public SocketClient(String host, int port, ComputeUnit computeUnit) {
        this.computeUnit = computeUnit;
        this.running = false;
        this.eventLoops = new ArrayList<>();
        this.host = host;
        this.port = port;
    }

    public void launch() {
        if (running) {
            return;
        }
        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EventLoopGroup worker = new NioEventLoopGroup();
                    eventLoops.add(worker);
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(worker);
                    bootstrap.channel(NioSocketChannel.class);
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.handler(new MessageChannelInitializer(computeUnit));
                    ChannelFuture channelFuture = bootstrap.connect(host, port);
                    channelFuture.channel().closeFuture().sync(); // block until all EventLoopGroup shutdown
                } catch (InterruptedException ignore) {
                } finally {
                    running = false;
                }
            }
        }).start();
    }

    @Override
    public void shutdown() {
        for (EventLoopGroup eventLoop : eventLoops) {
            eventLoop.shutdownGracefully();
        }
        eventLoops.clear();
    }
}
