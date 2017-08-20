package com.silverhetch.horae.upnp;

import com.sun.istack.internal.Nullable;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
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
public class HoraeDevice implements UPnPDevice {
    /**
     * Null if the running flag is false.
     * Notice that if this class is going more complicate then this, consider to refactor this class.
     */
    @Nullable
    private UpnpService upnpService;
    private final int socketServerPort;
    private boolean running;

    public HoraeDevice(int socketServerPort) {
        this.running = false;
        this.socketServerPort = socketServerPort;
    }

    @Override
    public void launch() {
        if (running) {
            return;
        }
        running = true;
        try {
            upnpService = new UpnpServiceImpl();
            Registry registry = upnpService.getRegistry();
            registry.addDevice(createDevice());
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDevice createDevice() throws ValidationException {
        UDN udn = UDN.uniqueSystemIdentifier("Horae");
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
        LocalService<Horae> localService = new AnnotationLocalServiceBinder().read(Horae.class);
        localService.setManager(new DefaultServiceManager<>(localService, Horae.class));
        localService.getManager().getImplementation().setPort(socketServerPort);
        return new LocalDevice(identity, type, deviceDetails, localService);
    }

    @Override
    public void shutdown() {
        if (!running) {
            return;
        }
        upnpService.shutdown();
        running = false;
    }
}

