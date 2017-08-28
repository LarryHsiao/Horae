package com.silverhetch.horae;

import com.silverhetch.horae.socket.MessageListener;
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
public class AutoConnectionDeviceImpl implements AutoConnectionDevice, DeviceListener {
    private final Map<String, RemoteDevice> remoteDeviceMap;
    private final ControlPoint controlPoint;
    private final SocketConnection socketConnection;
    private final MessageListener messageListener;
    private final DeviceStatusListener deviceStatusListener;
    private final SocketDevice serverDevice;
    private DeviceStatus deviceStatus;
    private SocketDevice targetDevice;


    public AutoConnectionDeviceImpl(HoraeUPnP horaeUPnP, SocketConnection socketConnection, MessageListener messageListener, DeviceStatusListener deviceStatusListener) {
        this.remoteDeviceMap = new HashMap<>();
        this.controlPoint = horaeUPnP.createControlPoint(this);
        this.socketConnection = socketConnection;
        this.messageListener = messageListener;
        this.deviceStatusListener = deviceStatusListener;
        this.deviceStatus = new DeviceStatusImpl(true);
        this.serverDevice = socketConnection.server(horaeUPnP, messageListener);
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
        remoteDeviceMap.put(remoteDevice.identity(), remoteDevice);
        changeTargetDeviceIfNeeded();
    }

    @Override
    public void onDeviceLeave(RemoteDevice remoteDevice) {
        remoteDeviceMap.remove(remoteDevice.identity());
        changeTargetDeviceIfNeeded();
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
            boolean master = (serverDevice.priority() == newTarget.priority());
            deviceStatus = new DeviceStatusImpl(master);
            deviceStatusListener.onStatusChanged(deviceStatus);
        }
    }

    private SocketDevice determineTargetDevice() {
        SocketDevice socketDevice = serverDevice;
        for (Map.Entry<String, RemoteDevice> deviceEntry : remoteDeviceMap.entrySet()) {
            if (deviceEntry.getValue().priority() > socketDevice.priority()) {
                RemoteDevice remoteDevice = deviceEntry.getValue();
                socketDevice = socketConnection.client(remoteDevice.host(), remoteDevice.port(), remoteDevice.priority(), messageListener);
            }
        }
        return socketDevice;
    }
}
