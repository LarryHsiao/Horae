package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;

public class HoraeUPnPImpl implements HoraeUPnP {
    private final UpnpService upnpService;

    public HoraeUPnPImpl(){
        this.upnpService = new UpnpServiceImpl();
    }

    @Override
    public Device device(int socketPort) {
        return new HoraeDevice(upnpService,socketPort);
    }

    @Override
    public ControlPoint controlPoint(DeviceListener listener) {
        return new HoraeControlPoint(upnpService,listener);
    }

    @Override
    public void shutdown() {
        upnpService.shutdown();
    }
}
