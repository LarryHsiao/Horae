package com.silverhetch.horae.socket;

public interface SocketConnection {
    SocketDevice server(int port, MessageListener messageListener);
    SocketDevice client(String host, int port, MessageListener messageListener);
}
