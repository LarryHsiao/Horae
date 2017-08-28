package com.silverhetch.horae.autoconnection;

import com.silverhetch.horae.socket.SocketEvent;
import com.silverhetch.horae.socket.SocketConnection;
import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.upnp.ControlPoint;
import com.silverhetch.horae.upnp.DeviceListener;
import com.silverhetch.horae.upnp.HoraeUPnP;
import com.silverhetch.horae.upnp.RemoteDevice;

import java.util.HashMap;
import java.util.Map;

/**
 * Auto change the socketDevice to Service/client according there is Horae device in the network, launch SocketServer when no device exist.
 */
public class AutoConnectionDeviceImpl implements AutoConnectionDevice, DeviceListener, SocketEvent {
    private final Map<Integer, RemoteDevice> remoteDeviceMap;
    private final ControlPoint controlPoint;
    private final SocketConnection socketConnection;
    private final SocketEvent socketEvent;
    private final DeviceStatusListener deviceStatusListener;
    private final SocketDevice serverDevice;
    private DeviceStatus deviceStatus;
    private SocketDevice targetDevice;


    public AutoConnectionDeviceImpl(HoraeUPnP horaeUPnP, SocketConnection socketConnection, SocketEvent socketEvent, DeviceStatusListener deviceStatusListener) {
        this.remoteDeviceMap = new HashMap<>();
        this.controlPoint = horaeUPnP.createControlPoint(this);
        this.socketConnection = socketConnection;
        this.socketEvent = socketEvent;
        this.deviceStatusListener = deviceStatusListener;
        this.deviceStatus = new DeviceStatusImpl(true);
        this.serverDevice = socketConnection.server(horaeUPnP, socketEvent);
        this.targetDevice = serverDevice;
    }

    @Override
    public DeviceStatus deviceStatus() {
        return deviceStatus;
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
                targetDevice.launch();
            }
        }).start();
    }

    @Override
    public void sendMessage(String message) {
        targetDevice.sendMessage(message);
    }

    @Override
    public void shutdown() {
        controlPoint.shutdown();
        targetDevice.shutdown();
    }

    @Override
    public void onDeviceDiscovered(RemoteDevice remoteDevice) {
        remoteDeviceMap.put(remoteDevice.priority(), remoteDevice);
        changeTargetDeviceIfNeeded();
    }

    @Override
    public void onDeviceLeave(RemoteDevice remoteDevice) {
        remoteDeviceMap.remove(remoteDevice.priority());
        changeTargetDeviceIfNeeded();
    }

    @Override
    public void onReceive(String message) {
        socketEvent.onReceive(message);
    }

    @Override
    public void onError(Throwable throwable) {
        if (!isLocalServer(targetDevice)) {
            remoteDeviceMap.remove(targetDevice.priority());
            changeTargetDeviceIfNeeded();
        } else {
            socketEvent.onError(throwable);
        }
    }

    /**
     * Determine the device which has highest priority and connect to it if it is not connected.
     */
    private void changeTargetDeviceIfNeeded() {
        SocketDevice newTarget = determineTargetDevice();
        if (targetDevice.priority() != newTarget.priority()) {
            targetDevice.shutdown();
            targetDevice = newTarget;
            launchSocketDeviceWithThread();
            deviceStatus = new DeviceStatusImpl(isLocalServer(newTarget));
            deviceStatusListener.onStatusChanged(deviceStatus);
        }
    }

    private SocketDevice determineTargetDevice() {
        SocketDevice socketDevice = serverDevice;
        for (Map.Entry<Integer, RemoteDevice> deviceEntry : remoteDeviceMap.entrySet()) {
            if (deviceEntry.getValue().priority() > socketDevice.priority()) {
                RemoteDevice remoteDevice = deviceEntry.getValue();
                socketDevice = socketConnection.client(remoteDevice.host(), remoteDevice.port(), remoteDevice.priority(), this);
            }
        }
        return socketDevice;
    }

    private boolean isLocalServer(SocketDevice socketDevice) {
        return serverDevice.priority() == socketDevice.priority();
    }
}
