package com.silverhetch.horae;

import com.silverhetch.clotho.log.Log;
import com.silverhetch.clotho.observable.Observable;
import com.silverhetch.horae.autoconnection.AutoConnectionDevice;
import com.silverhetch.horae.autoconnection.AutoConnectionDeviceImpl;
import com.silverhetch.horae.autoconnection.DeviceStatus;
import com.silverhetch.horae.socket.SocketConnectionImpl;
import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.socket.SocketEvent;
import com.silverhetch.horae.upnp.HoraeUPnP;

import java.util.LinkedList;
import java.util.List;

public class HoraeImpl implements Horae, SocketEvent {
    private final AutoConnectionDevice socketDevice;
    private final HoraeUPnP horaeUPnP;
    private List<MessageHandle> messageHandles;
    private final Log log;

    public HoraeImpl(HoraeUPnP horaeUPnP, Log log) {
        this.horaeUPnP = horaeUPnP;
        this.socketDevice = new AutoConnectionDeviceImpl(
                this.horaeUPnP,
                new SocketConnectionImpl(),
                this);
        this.messageHandles = new LinkedList<>();
        this.log = log;
    }

    @Override
    public Observable<DeviceStatus> deviceStatusObservable() {
        return socketDevice.observable();
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
    public void addMessageHandle(MessageHandle messageHandle) {
        if (messageHandles.contains(messageHandle)) {
            return;
        }
        messageHandles.add(messageHandle);
    }

    @Override
    public void removeMessageHandle(MessageHandle messageHandle) {
        messageHandles.remove(messageHandle);
    }

    @Override
    public void onReceive(String rawMessage) {
        try {
            Message jsonReceivedMessage = new ReceivedMessage(rawMessage);
            for (MessageHandle messageHandle : messageHandles) {
                if (messageHandle.messageType().equals(jsonReceivedMessage.messageType())) {
                    messageHandle.onReceive(jsonReceivedMessage.content());
                }
            }
        } catch (Exception e) {
            log.error("Message handle failed " + e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
