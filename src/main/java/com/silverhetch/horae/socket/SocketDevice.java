package com.silverhetch.horae.socket;

public interface SocketDevice {
    void launch();
    void sendMessage(String message);
    void shutdown();
}
