package com.silverhetch.horae;

import com.silverhetch.horae.socket.MessageListener;
import com.silverhetch.horae.socket.SocketConnectionImpl;
import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.upnp.*;

public class Horae implements MessageListener {
    private final SocketDevice socketDevice;
    private final HoraeUPnP horaeUPnP;

    public Horae() {
        this.horaeUPnP = new HoraeUPnPImpl();
        this.socketDevice = new AutoConnectionSocketDevice(
                horaeUPnP,
                new SocketConnectionImpl(),
                this);
    }

    public void launch() {
        socketDevice.launch();
    }

    public void shutdown() {
        socketDevice.shutdown();
        horaeUPnP.shutdown();
    }

    @Override
    public void onReceive(String message) {
        socketDevice.sendMessage("This is response of " + message);
    }
}
