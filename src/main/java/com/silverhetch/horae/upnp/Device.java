package com.silverhetch.horae.upnp;

public interface Device {
    void launch();
    void shutdown();
    int priority();
}
