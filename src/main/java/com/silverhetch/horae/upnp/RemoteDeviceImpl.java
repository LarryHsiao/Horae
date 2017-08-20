package com.silverhetch.horae.upnp;

class RemoteDeviceImpl implements RemoteDevice {
    private org.fourthline.cling.model.meta.RemoteDevice remoteDevice;

    public RemoteDeviceImpl(org.fourthline.cling.model.meta.RemoteDevice remoteDevice) {
        this.remoteDevice = remoteDevice;
    }

    @Override
    public String host() {
        return remoteDevice.getIdentity().getDiscoveredOnLocalAddress().getHostAddress();
    }

    @Override
    public String identity() {
        return remoteDevice.getIdentity().toString();
    }
}
