package com.silverhetch.horae.socket;

public class SocketConnectionImpl implements SocketConnection {
    @Override
    public SocketDevice server(int port, MessageListener messageListener) {
        return new SocketServer(port, messageListener);
    }

    @Override
    public SocketDevice client(String host, int port, MessageListener messageListener) {
        return new SocketClient(host, port, messageListener);
    }
}
