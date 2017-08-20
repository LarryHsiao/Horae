package com.silverhetch.horae.upnp;

public interface HoraeUPnP {
    boolean isLocalDevice(String host);
    Device createDevice(int socketPort);
    ControlPoint createControlPoint(DeviceListener listener);
    void shutdown();
}
