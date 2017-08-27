package com.silverhetch.horae.upnp;

class RemoteDeviceImpl implements RemoteDevice {
    private final int port;
    private org.fourthline.cling.model.meta.RemoteDevice remoteDevice;

    public RemoteDeviceImpl(org.fourthline.cling.model.meta.RemoteDevice remoteDevice, int port) {
        this.remoteDevice = remoteDevice;
        this.port = port;
    }

    @Override
    public String host() {
        return remoteDevice.getIdentity().getDiscoveredOnLocalAddress().getHostAddress();
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public int priority() {
        return remoteDevice.getIdentity().toString().hashCode();
    }

    @Override
    public String identity() {
        return remoteDevice.getIdentity().toString();
    }
}
