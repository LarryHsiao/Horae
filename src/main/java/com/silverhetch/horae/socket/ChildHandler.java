package com.silverhetch.horae.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Vector;

class ChildHandler extends ChannelInitializer<SocketChannel> {

    private final SocketEvent socketEvent;
    private final Vector<MessageHandler> messageHandlers;

    public ChildHandler(SocketEvent socketEvent) {
        this.socketEvent = socketEvent;
        this.messageHandlers = new Vector<>();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("farmer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        MessageHandler messageHandler = new MessageHandler(socketEvent);
        pipeline.addLast("onReceive", messageHandler);
        messageHandlers.add(messageHandler);
    }

    public void sendMessage(String message){
        for (MessageHandler messageHandler : messageHandlers) {
            messageHandler.sendMessage(message);
        }
    }
}
