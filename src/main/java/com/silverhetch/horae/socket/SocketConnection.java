package com.silverhetch.horae.socket;

public interface SocketConnection {
    SocketDevice server(int port, ComputeUnit computeUnit);
    SocketDevice client(String host, int port, ComputeUnit computeUnit);
}
