package com.silverhetch.horae;

public interface MessageHandle {
    int messageType();
    void onReceive(String message);
}
