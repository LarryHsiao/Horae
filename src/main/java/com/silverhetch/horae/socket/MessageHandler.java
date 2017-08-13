package com.silverhetch.horae.socket;

import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {

    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}
