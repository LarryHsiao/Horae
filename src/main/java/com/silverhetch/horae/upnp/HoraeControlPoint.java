package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.registry.*;

class HoraeControlPoint implements ControlPoint {
    /**
     * Null if the running flag is false.
     * Notice that if this class is going more complicate then this, consider to refactor this class.
     */
    private UpnpService upnpService;
    private final HoraeDiscoverListener listener;
    private boolean running;

    public HoraeControlPoint(HoraeDiscoverListener listener) {
        this.listener = listener;
        this.running = false;
    }

    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
        upnpService = new UpnpServiceImpl(new DefaultRegistryListener() {
            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                super.remoteDeviceAdded(registry, device);
                listener.onDeviceDiscovered(new RemoteDeviceImpl(device));
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                super.remoteDeviceRemoved(registry, device);
                listener.onDeviceDiscovered(new RemoteDeviceImpl(device));
            }
        });
        upnpService.getControlPoint().search(
                new UDADeviceTypeHeader(new UDADeviceType("Horae"))
        );
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
        upnpService.shutdown();
        upnpService = null;
        running = false;
    }
}