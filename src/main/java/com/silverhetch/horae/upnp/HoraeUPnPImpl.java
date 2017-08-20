package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.types.UDN;

public class HoraeUPnPImpl implements HoraeUPnP {
    private final UpnpService upnpService;

    public HoraeUPnPImpl() {
        this.upnpService = new UpnpServiceImpl();
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
        return new HoraeDevice(upnpService, socketPort);
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
