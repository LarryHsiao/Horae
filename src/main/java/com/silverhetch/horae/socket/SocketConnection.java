package com.silverhetch.horae.socket;

import com.silverhetch.horae.upnp.HoraeUPnP;

public interface SocketConnection {
    SocketDevice server(HoraeUPnP horaeUPnP, SocketEvent socketEvent);
    SocketDevice client(String host, int port,int priority, SocketEvent socketEvent);
}
