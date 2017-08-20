package com.silverhetch.horae;

import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.upnp.Device;

public class UPnPSocketDevice implements SocketDevice {
    private final SocketDevice socketDevice;
    private final Device upnpDevice;

    public UPnPSocketDevice(SocketDevice socketDevice, Device upnpDevice) {
        this.socketDevice = socketDevice;
        this.upnpDevice = upnpDevice;
    }

    @Override
    public void launch() {
        upnpDevice.launch();
        socketDevice.launch();
    }

    @Override
    public void sendMessage(String message) {
        socketDevice.sendMessage(message);
    }

    @Override
    public void shutdown() {
        socketDevice.shutdown();
        upnpDevice.shutdown();
    }
}
