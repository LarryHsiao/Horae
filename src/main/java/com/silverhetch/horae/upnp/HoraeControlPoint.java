package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.*;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceId;
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
        public void remoteDeviceAdded(Registry registry, final RemoteDevice device) {
            super.remoteDeviceAdded(registry, device);
            try {
                Service service = device.findService(new UDAServiceId("Horae"));
                Action action = service.getAction("ConnectionPort");
                ActionInvocation connectionPortInvoking = new ActionInvocation(action); // nothing i can do with the type warning.The cling`s style.
                upnpService.getControlPoint().execute(new ActionCallback(connectionPortInvoking) {
                    @Override
                    public void success(ActionInvocation invocation) {
                        ActionArgumentValue argument = invocation.getOutput("ConnectionPort");
                        Integer port = (Integer) argument.getValue();
                        listener.onDeviceDiscovered(new RemoteDeviceImpl(device, port));
                    }

                    @Override
                    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
                        // ignore service not recognized
                    }
                });
            }catch (Exception ignore){ // ignore if service not recognized.
            }
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            super.remoteDeviceRemoved(registry, device);
            listener.onDeviceLeave(new RemoteDeviceImpl(device, 0));
        }
    }
}