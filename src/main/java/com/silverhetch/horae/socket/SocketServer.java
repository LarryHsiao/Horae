package com.silverhetch.horae.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Vector;

class SocketServer implements SocketDevice {
    private final int port;
    private final Vector<EventLoopGroup> eventLoops;
    private final ChildHandler childHandler;
    private boolean running;

    // TODO: 2017/8/14 replace computeUnit with simple request/response interface
    public SocketServer(int port, ComputeUnit computeUnit) {
        this.port = port;
        this.eventLoops = new Vector<>();
        this.childHandler = new ChildHandler(computeUnit);
        this.running = false;
    }

    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EventLoopGroup entranceLoop = new NioEventLoopGroup();
                    EventLoopGroup workerLoop = new NioEventLoopGroup();
                    eventLoops.add(workerLoop);
                    eventLoops.add(entranceLoop);
                    ServerBootstrap server = new ServerBootstrap();
                    server.group(entranceLoop, workerLoop);
                    server.channel(NioServerSocketChannel.class);
                    server.childHandler(childHandler);
                    server.option(ChannelOption.SO_BACKLOG, 128);
                    server.childOption(ChannelOption.SO_KEEPALIVE, true);
                    ChannelFuture channelFuture = server.bind(port).sync();
                    channelFuture.channel().closeFuture().sync(); // will block until all EventViewGroup shutdown
                } catch (InterruptedException ignore) {
                } finally {
                    running = false;
                }
            }
        }).start();

    }

    @Override
    public void sendMessage(String message) {
        childHandler.sendMessage(message);
    }

    @Override
    public void shutdown() {
        for (EventLoopGroup eventLoop : eventLoops) {
            eventLoop.shutdownGracefully();
        }
        eventLoops.clear();
    }
}
