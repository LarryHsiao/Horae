package com.silverhetch.horae.device;

public interface RemoteDevice {
    String name();
    void sendMessage(String message);
}
