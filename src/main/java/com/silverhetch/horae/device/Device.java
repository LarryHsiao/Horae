package com.silverhetch.horae.device;

import java.util.List;

public interface Device {
    void enable();
    void disable();
    List<RemoteDevice> remoteDevices();
}
