package com.silverhetch.horae;

import com.silverhetch.horae.socket.MessageListener;
import com.silverhetch.horae.socket.SocketConnectionImpl;
import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.upnp.*;

public class HoraeImpl implements Horae, MessageListener {
    private final SocketDevice socketDevice;
    private final HoraeUPnP horaeUPnP;

    public HoraeImpl(HoraeUPnP horaeUPnP, DeviceStatusListener deviceStatusListener) {
        this.horaeUPnP = horaeUPnP;
        this.socketDevice = new AutoConnectionSocketDevice(
                this.horaeUPnP,
                new SocketConnectionImpl(),
                this,
                deviceStatusListener);
    }

    @Override
    public void launch() {
        socketDevice.launch();
    }

    @Override
    public void shutdown() {
        socketDevice.shutdown();
        horaeUPnP.shutdown();
    }

    @Override
    public void onReceive(String message) {
        System.out.println("REceived message " + message);
        socketDevice.sendMessage("This is response of " + message);
    }
}
