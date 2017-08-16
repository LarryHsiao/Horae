package com.silverhetch.horae.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Vector;

class MessageHandler extends ChannelInboundHandlerAdapter {
    private final Vector<ChannelHandlerContext> channels;
    private final ComputeUnit computeUnit;

    public MessageHandler(ComputeUnit computeUnit) {
        this.computeUnit = computeUnit;
        this.channels = new Vector<>();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        channels.add(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        channels.remove(ctx);
    }

    public void sendMessage(String message) {
        for (ChannelHandlerContext channel : channels) {
            channel.writeAndFlush("1234");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object inboundMessage) throws Exception {
        try {
            String inbound = (String) inboundMessage;
            String result = computeUnit.compute(inbound);
            ctx.writeAndFlush(result);
        } finally {
            ReferenceCountUtil.release(inboundMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
