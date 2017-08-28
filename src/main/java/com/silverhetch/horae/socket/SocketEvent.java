package com.silverhetch.horae.socket;

public interface SocketEvent {
    void onReceive(String message);
    void onError(Throwable throwable);
}
