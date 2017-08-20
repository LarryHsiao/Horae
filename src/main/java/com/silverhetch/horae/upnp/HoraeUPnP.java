package com.silverhetch.horae.upnp;

public interface HoraeUPnP {
    Device device(int socketPort);
    ControlPoint controlPoint(HoraeDiscoverListener listener);
}
