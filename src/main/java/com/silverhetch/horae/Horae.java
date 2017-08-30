package com.silverhetch.horae;

import com.silverhetch.horae.autoconnection.DeviceStatus;

public interface Horae {
    void launch();
    void shutdown();
    void request(Message message);
    DeviceStatus deviceStatus();
}
