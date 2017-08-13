package com.silverhetch.horae;

import java.util.List;

public interface HoraeDevice {
    void enable();
    void disable();
    List<RemoteHoraeDevice> remoteDevices();
    void sendMessage(String message);
}
