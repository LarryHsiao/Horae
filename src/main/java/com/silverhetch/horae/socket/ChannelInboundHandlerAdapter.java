package com.silverhetch.horae.socket;

import io.netty.channel.ChannelHandlerContext;

class ChannelInboundHandlerAdapter extends io.netty.channel.ChannelInboundHandlerAdapter {
    private final MessageHandler messageHandler;

    public ChannelInboundHandlerAdapter(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageHandler.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        messageHandler.exceptionCaught(ctx, cause);
    }
}
