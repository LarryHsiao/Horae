package com.silverhetch.horae;

import com.silverhetch.clotho.log.Log;
import com.silverhetch.horae.autoconnection.*;
import com.silverhetch.horae.socket.SocketEvent;
import com.silverhetch.horae.socket.SocketConnectionImpl;
import com.silverhetch.horae.upnp.*;

public class HoraeImpl implements Horae, SocketEvent {
    private final AutoConnectionDevice socketDevice;
    private final HoraeUPnP horaeUPnP;
    private final MessageHandle messageHandle;
    private final Log log;

    public HoraeImpl(HoraeUPnP horaeUPnP, Log log, DeviceStatusListener listener, MessageHandle... messageHandles) {
        this.horaeUPnP = horaeUPnP;
        this.socketDevice = new AutoConnectionDeviceImpl(
                this.horaeUPnP,
                new SocketConnectionImpl(),
                this,
                listener);
        this.messageHandle = new MultiMessageHandle(messageHandles);
        this.log = log;
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
    public void request(Message message) {
        RequestMessage requestMessage = new RequestMessage(message.messageType(), message.content());
        socketDevice.sendMessage(requestMessage.json());
    }

    @Override
    public void onReceive(String message) {
        try {
            messageHandle.onReceive(message);
        } catch (Exception e) {
            log.error("Message handle failed " + e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
