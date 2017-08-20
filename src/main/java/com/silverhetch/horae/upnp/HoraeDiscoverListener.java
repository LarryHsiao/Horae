package com.silverhetch.horae.upnp;

public interface HoraeDiscoverListener {
    void onDeviceDiscovered(RemoteDevice remoteDevice);
    void onDeviceLeave(RemoteDevice remoteDevice);
}
