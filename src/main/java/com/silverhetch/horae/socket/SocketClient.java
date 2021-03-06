package com.silverhetch.horae.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

class SocketClient implements SocketDevice {
    private final ChildHandler childHandler;
    private final List<EventLoopGroup> eventLoops;
    private final String host;
    private final int port;
    private final int priority;
    private boolean running;

    public SocketClient(String host, int port, int priority, SocketEvent socketEvent) {
        this.childHandler = new ChildHandler(socketEvent);
        this.priority = priority;
        this.running = false;
        this.eventLoops = new ArrayList<>();
        this.host = host;
        this.port = port;
    }

    /**
     * Note: This method will block the thread until channel closed.
     */
    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
        try {
            EventLoopGroup worker = new NioEventLoopGroup();
            eventLoops.add(worker);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(childHandler);
            ChannelFuture channelFuture = bootstrap.connect(host, port);
            channelFuture.channel().closeFuture().sync(); // block until all EventLoopGroup shutdown
        } catch (InterruptedException ignore) {
        } finally {
            running = false;
        }
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public void shutdown() {
        for (EventLoopGroup eventLoop : eventLoops) {
            eventLoop.shutdownGracefully();
        }
        eventLoops.clear();
    }

    @Override
    public void sendMessage(String message) {
        childHandler.sendMessage(message+"\n");
    }
}
