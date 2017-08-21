package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.registry.*;

class HoraeControlPoint implements ControlPoint {
    private final UpnpService upnpService;
    private final DeviceListener listener;
    private final RegisterListener registerListener;
    private boolean running;

    public HoraeControlPoint(UpnpService upnpService, DeviceListener listener) {
        this.registerListener = new RegisterListener();
        this.upnpService = upnpService;
        this.listener = listener;
        this.running = false;
    }

    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
        upnpService.getRegistry().addListener(registerListener);
        upnpService.getControlPoint().search(
                new UDADeviceTypeHeader(new UDADeviceType("Horae"))
        );
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
        upnpService.getRegistry().removeListener(registerListener);
        running = false;
    }

    private class RegisterListener extends DefaultRegistryListener {
        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            super.remoteDeviceAdded(registry, device);
            listener.onDeviceDiscovered(new RemoteDeviceImpl(device));
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            super.remoteDeviceRemoved(registry, device);
            listener.onDeviceLeave(new RemoteDeviceImpl(device));
        }
    }
}