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
    private final MessageHandler[] messageHandlers;
    private boolean running;

    public SocketServer(int port, MessageHandler... messageHandlers) {
        this.port = port;
        this.eventLoops = new Vector<>();
        this.messageHandlers = messageHandlers;
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
                ServerBootstrap server = new ServerBootstrap();
                server.group(entranceLoop, workerLoop);
                server.channel(NioServerSocketChannel.class);
                server.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        for (MessageHandler messageHandler : messageHandlers) {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(messageHandler));
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
