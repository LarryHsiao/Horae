package com.silverhetch.horae.socket;

public class SocketConnectionImpl implements SocketConnection {
    @Override
    public SocketDevice server(int port, ComputeUnit computeUnit) {
        return new SocketServer(port, computeUnit);
    }

    @Override
    public SocketDevice client(String host, int port, ComputeUnit computeUnit) {
        return new SocketClient(host, port, computeUnit);
    }
}
