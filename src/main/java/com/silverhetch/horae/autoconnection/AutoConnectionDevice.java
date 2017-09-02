package com.silverhetch.horae.autoconnection;


import com.silverhetch.clotho.observable.Observable;

public interface AutoConnectionDevice {
    void launch();
    void sendMessage(String message);
    void shutdown();
    DeviceStatus deviceStatus();
    Observable<DeviceStatus> observable();

}
