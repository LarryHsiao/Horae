package com.silverhetch.horae;

import com.silverhetch.horae.socket.MessageListener;
import com.silverhetch.horae.socket.SocketConnection;
import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.upnp.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Auto change the socketDevice to Service/client according there is Horae device in the network, launch SocketServer when no device exist.
 */
public class AutoConnectionSocketDevice implements SocketDevice, DeviceListener {
    private final Map<String, RemoteDevice> remoteDeviceMap;
    private final ControlPoint controlPoint;
    private final SocketConnection socketConnection;
    private final HoraeUPnP horaeUPnP;
    private final MessageListener messageListener;
    private SocketDevice socketDevice;


    public AutoConnectionSocketDevice(HoraeUPnP horaeUPnP, SocketConnection socketConnection, MessageListener messageListener) {
        this.remoteDeviceMap = new HashMap<>();
        this.horaeUPnP = horaeUPnP;
        this.controlPoint = horaeUPnP.createControlPoint(this);
        this.socketConnection = socketConnection;
        this.messageListener = messageListener;
        this.socketDevice = createSocketServer();
    }

    @Override
    public void launch() {
        controlPoint.launch();
        launchSocketDeviceWithThread();
    }

    private void launchSocketDeviceWithThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketDevice.launch();
            }
        }).start();
    }

    @Override
    public void sendMessage(String message) {
        socketDevice.sendMessage(message);
    }

    @Override
    public void shutdown() {
        controlPoint.shutdown();
        socketDevice.shutdown();
    }

    @Override
    public void onDeviceDiscovered(RemoteDevice remoteDevice) {
//        if (remoteDeviceMap.size() == 0) {
//            socketDevice.shutdown();
//            socketDevice = socketConnection.client(remoteDevice.host(), 8912, messageListener);
//            launchSocketDeviceWithThread();
//        }
//        remoteDeviceMap.put(remoteDevice.identity(), remoteDevice);
    }

    @Override
    public void onDeviceLeave(RemoteDevice remoteDevice) {
//        remoteDeviceMap.remove(remoteDevice.identity());
//        if (remoteDeviceMap.size() == 0) {
//            socketDevice.shutdown();
//            socketDevice = createSocketServer();
//            launchSocketDeviceWithThread();
//        }
    }

    private SocketDevice createSocketServer() {
        return socketConnection.server(horaeUPnP, messageListener);
    }
}
