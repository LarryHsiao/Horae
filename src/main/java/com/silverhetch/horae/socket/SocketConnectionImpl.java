package com.silverhetch.horae.socket;

import com.silverhetch.horae.upnp.HoraeUPnP;

public class SocketConnectionImpl implements SocketConnection {
    @Override
    public SocketDevice server(HoraeUPnP horaeUPnP, SocketEvent socketEvent) {
        return new SocketServer(horaeUPnP, socketEvent);
    }

    @Override
    public SocketDevice client(String host, int port, int priority, SocketEvent socketEvent) {
        return new SocketClient(host, port, priority, socketEvent);
    }
}
