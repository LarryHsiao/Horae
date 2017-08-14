package com.silverhetch.horae.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Vector;

public class SocketServer {
    private final int port;
    private final Vector<EventLoopGroup> eventLoops;
    private final ComputeUnit[] computeUnit;
    private boolean running;

    // TODO: 2017/8/14 replace computeUnit with simple request/response interface
    public SocketServer(int port, ComputeUnit... computeUnit) {
        this.port = port;
        this.eventLoops = new Vector<>();
        this.computeUnit = computeUnit;
        this.running = false;
    }

    public void launch() {
        if (running) {
            return;
        }
        running = true;
        new Thread(() -> {
            try {
                EventLoopGroup entranceLoop = new NioEventLoopGroup();
                EventLoopGroup workerLoop = new NioEventLoopGroup();
                eventLoops.add(workerLoop);
                eventLoops.add(entranceLoop);
                ServerBootstrap server = new ServerBootstrap();
                server.group(entranceLoop, workerLoop);
                server.channel(NioServerSocketChannel.class);
                server.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        for (ComputeUnit computeUnit : SocketServer.this.computeUnit) {
                            ch.pipeline().addLast(new MessageHandler(computeUnit));
                        }

                    }
                });
                server.option(ChannelOption.SO_BACKLOG, 128);
                server.childOption(ChannelOption.SO_KEEPALIVE, true);
                ChannelFuture channelFuture = server.bind(port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException ignore) {
            } finally {
                running = false;
            }
        }).start();

    }

    public void shutdown() {
        for (EventLoopGroup eventLoop : eventLoops) {
            eventLoop.shutdownGracefully();
        }
        eventLoops.clear();
    }
}
