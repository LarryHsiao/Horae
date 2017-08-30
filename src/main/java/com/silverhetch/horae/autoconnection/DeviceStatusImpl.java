package com.silverhetch.horae.autoconnection;

class DeviceStatusImpl implements DeviceStatus {
    private final boolean master;

    public DeviceStatusImpl(boolean master) {
        this.master = master;
    }

    @Override
    public boolean isMaster() {
        return master;
    }
}
