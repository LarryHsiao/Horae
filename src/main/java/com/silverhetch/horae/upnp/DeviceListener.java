package com.silverhetch.horae.upnp;

public interface DeviceListener {
    void onDeviceDiscovered(RemoteDevice remoteDevice);
    void onDeviceLeave(RemoteDevice remoteDevice);
}
