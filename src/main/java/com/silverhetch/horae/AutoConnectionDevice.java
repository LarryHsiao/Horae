package com.silverhetch.horae;

public interface AutoConnectionDevice {
    void launch();
    void sendMessage(String message);
    void shutdown();
    DeviceStatus deviceStatus();
}
