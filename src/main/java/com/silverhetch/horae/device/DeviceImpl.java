package com.silverhetch.horae.device;

import java.util.Collections;
import java.util.List;

public class DeviceImpl implements Device {
    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public List<RemoteDevice> remoteDevices() {
        return Collections.singletonList(new RemoteDeviceImpl());
    }
}
