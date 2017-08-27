package com.silverhetch.horae.socket;

import com.silverhetch.horae.upnp.HoraeUPnP;

public interface SocketConnection {
    SocketDevice server(HoraeUPnP horaeUPnP, MessageListener messageListener);
    SocketDevice client(String host, int port,int priority, MessageListener messageListener);
}
