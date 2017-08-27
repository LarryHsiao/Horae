package com.silverhetch.horae.upnp;

public interface RemoteDevice {
    String identity();
    String host();
    int port();
    int priority();
}
