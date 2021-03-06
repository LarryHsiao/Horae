package com.silverhetch.horae.upnp;

import org.fourthline.cling.binding.annotations.*;

/**
 * Do NOT initial this class with constructor.
 * This class auto creates by Cling library.
 */
@UpnpService(
        serviceId = @UpnpServiceId("Horae"),
        serviceType = @UpnpServiceType(value = "Horae", version = 1)
)
public class HoraeService {
    @UpnpStateVariable(defaultValue = "8912")
    private int connectionPort;

    public HoraeService() {
        this.connectionPort = 8912;
    }

    public void setPort(int port) {
        this.connectionPort = port;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ConnectionPort"))
    public int ConnectionPort() {
        return connectionPort;
    }
}
