package com.silverhetch.horae.socket;

import com.silverhetch.horae.upnp.Device;
import com.silverhetch.horae.upnp.HoraeUPnP;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.BindException;
import java.util.Vector;

class SocketServer implements SocketDevice {
    private final Vector<EventLoopGroup> eventLoops;
    private final ChildHandler childHandler;
    private final HoraeUPnP horaeUPnP;
    private int port;
    private boolean running;

    public SocketServer(HoraeUPnP horaeUPnP, MessageListener messageListener) {
        this.port = 8912;
        this.horaeUPnP = horaeUPnP;
        this.eventLoops = new Vector<>();
        this.childHandler = new ChildHandler(messageListener);
        this.running = false;
    }

    /**
     * Note: this method will block until the channel closed.
     */
    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
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
        launchWithUpnpDevice(server);
    }

    /**
     * UPnP Server available as long as Socket Server available.
     */
    private void launchWithUpnpDevice(ServerBootstrap server) {
        Device device = null;
        try {
            ChannelFuture channelFuture = bindPort(server);
            device = horaeUPnP.createDevice(port);
            device.launch();
            channelFuture.channel().closeFuture().sync(); // will block until all EventViewGroup shutdown
        } catch (InterruptedException ignore) {
        } finally {
            if (device != null) {
                device.shutdown();
            }
            running = false;
        }
    }

    private ChannelFuture bindPort(ServerBootstrap server) throws InterruptedException {
        while (true) {
            try {
                return server.bind(port).sync();
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    throw e;
                }
                // Assume only has BindException goes through here.
                port++;
            }
        }
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
