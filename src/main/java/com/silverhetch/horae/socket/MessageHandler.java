package com.silverhetch.horae.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Vector;

class MessageHandler extends ChannelInboundHandlerAdapter {
    private final SocketEvent socketEvent;
    private final Vector<ChannelHandlerContext> channels;

    public MessageHandler(SocketEvent socketEvent) {
        this.socketEvent = socketEvent;
        this.channels = new Vector<>();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object inboundMessage) throws Exception {
        try {
            socketEvent.onReceive((String) inboundMessage);
        } finally {
            ReferenceCountUtil.release(inboundMessage);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channels.add(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channels.remove(ctx);
    }

    public void sendMessage(String message) {
        for (ChannelHandlerContext channel : channels) {
            channel.writeAndFlush(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        socketEvent.onError(cause);
        ctx.close();
    }
}
