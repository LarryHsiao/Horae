package com.silverhetch.horae.socket;

import com.silverhetch.horae.upnp.HoraeUPnP;

public class SocketConnectionImpl implements SocketConnection {
    @Override
    public SocketDevice server(HoraeUPnP horaeUPnP, MessageListener messageListener) {
        return new SocketServer(horaeUPnP, messageListener);
    }

    @Override
    public SocketDevice client(String host, int port, int priority, MessageListener messageListener) {
        return new SocketClient(host, port, priority, messageListener);
    }
}
