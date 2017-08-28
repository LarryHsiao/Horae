package com.silverhetch.horae.autoconnection;

public interface AutoConnectionDevice {
    void launch();
    void sendMessage(String message);
    void shutdown();
    DeviceStatus deviceStatus();
}
