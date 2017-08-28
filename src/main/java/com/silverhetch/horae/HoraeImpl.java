package com.silverhetch.horae;
import com.silverhetch.horae.socket.SocketEvent;
import com.silverhetch.horae.socket.SocketConnectionImpl;
import com.silverhetch.horae.upnp.*;

public class HoraeImpl implements Horae, SocketEvent {
    private final AutoConnectionDevice socketDevice;
    private final HoraeUPnP horaeUPnP;

    public HoraeImpl(HoraeUPnP horaeUPnP, DeviceStatusListener deviceStatusListener) {
        this.horaeUPnP = horaeUPnP;
        this.socketDevice = new AutoConnectionDeviceImpl(
                this.horaeUPnP,
                new SocketConnectionImpl(),
                this,
                deviceStatusListener);
    }

    @Override
    public DeviceStatus deviceStatus() {
        return socketDevice.deviceStatus();
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

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
