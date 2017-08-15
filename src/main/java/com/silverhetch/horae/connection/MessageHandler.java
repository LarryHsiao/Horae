package com.silverhetch.horae.connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

class MessageHandler extends ChannelInboundHandlerAdapter {

    private final ComputeUnit computeUnit;

    public MessageHandler(ComputeUnit computeUnit) {
        this.computeUnit = computeUnit;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object inboundMessage) throws Exception {
        try {
            String inbound = (String) inboundMessage;
            String result = computeUnit.compute(inbound);
            System.out.println(inbound +" :: "+ result);
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
