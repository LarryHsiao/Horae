package com.silverhetch.horae.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.Registry;

/**
 * This class used to let other device connecting to this device.
 */
class HoraeDevice implements Device {
    private final UpnpService upnpService;
    private final int socketServerPort;
    private final UDN udn;
    private boolean running;

    public HoraeDevice(UDN udn, UpnpService upnpService, int socketServerPort) {
        this.upnpService = upnpService;
        this.running = false;
        this.socketServerPort = socketServerPort;
        this.udn = udn;
    }

    public HoraeDevice(UpnpService upnpService, int socketServerPort) {
        this(UDN.uniqueSystemIdentifier("Horae"), upnpService, socketServerPort);
    }

    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
        try {
            Registry registry = upnpService.getRegistry();
            registry.addDevice(createDevice());
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDevice createDevice() throws ValidationException {
        DeviceIdentity identity = new DeviceIdentity(udn);
        DeviceType type = new UDADeviceType("Horae", 1);
        DeviceDetails deviceDetails = new DeviceDetails(
                "Horae",
                new ManufacturerDetails("SilverHetch", "https://silverhetch.com"),
                new ModelDetails(
                        "Horae",
                        "Horae",
                        "v1"
                )
        );

        // Nothing we can do with the type warning below. This is the cling`s style.
        LocalService<HoraeService> localService = new AnnotationLocalServiceBinder().read(HoraeService.class);
        localService.setManager(new DefaultServiceManager<>(localService, HoraeService.class));
        localService.getManager().getImplementation().setPort(socketServerPort);
        return new LocalDevice(identity, type, deviceDetails, localService);
    }

    @Override
    public int priority() {
        return udn.toString().hashCode();
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
        upnpService.getRegistry().removeDevice(udn);
        running = false;
    }
}

