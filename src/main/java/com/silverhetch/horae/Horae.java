package com.silverhetch.horae;

import com.silverhetch.clotho.observable.Observable;
import com.silverhetch.horae.autoconnection.DeviceStatus;

public interface Horae {
    void launch();
    void shutdown();
    void request(Message message);
    DeviceStatus deviceStatus();
    Observable<DeviceStatus> deviceStatusObservable();
    void addMessageHandle(MessageHandle messageHandle);
    void removeMessageHandle(MessageHandle messageHandle);
}
