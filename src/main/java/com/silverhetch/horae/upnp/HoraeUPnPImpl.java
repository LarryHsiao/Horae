package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.types.UDN;

import java.util.UUID;

public class HoraeUPnPImpl implements HoraeUPnP {
    private final UpnpService upnpService;

    public HoraeUPnPImpl(UpnpService upnpService) {
        this.upnpService = upnpService;
    }

    @Override
    public boolean isLocalDevice(String identifyString) {
        for (LocalDevice localDevice : upnpService.getRegistry().getLocalDevices()) {
            if (localDevice.getIdentity().getUdn().equals(new UDN(identifyString))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Device createDevice(int socketPort) {
        if (ModelUtil.ANDROID_RUNTIME) {
            return new HoraeDevice(new UDN(UUID.randomUUID()), upnpService, socketPort);
        } else {
            return new HoraeDevice(upnpService, socketPort);
        }
    }

    @Override
    public ControlPoint createControlPoint(DeviceListener listener) {
        return new HoraeControlPoint(upnpService, listener);
    }

    @Override
    public void shutdown() {
        upnpService.shutdown();
    }
}
