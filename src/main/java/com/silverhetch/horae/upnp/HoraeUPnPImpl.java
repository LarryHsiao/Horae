package com.silverhetch.horae.upnp;

public class HoraeUPnPImpl implements HoraeUPnP {
    @Override
    public Device device(int socketPort) {
        return new HoraeDevice(socketPort);
    }

    @Override
    public ControlPoint controlPoint(HoraeDiscoverListener listener) {
        return new HoraeControlPoint(listener);
    }
}
