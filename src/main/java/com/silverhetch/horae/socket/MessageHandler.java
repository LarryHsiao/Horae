package com.silverhetch.horae.socket;

import io.netty.buffer.ByteBuf;
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
        ByteBuf in = (ByteBuf) inboundMessage;
        try {
            StringBuilder abc = new StringBuilder();
            while (in.isReadable()) {
                abc.append((char) in.readByte());
            }
            String result = computeUnit.compute(abc.toString());
            System.out.println(abc +" :: "+ result);
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
