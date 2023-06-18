package com.infinia.scrollup.driver.repository;


import java.net.InetAddress;
import java.util.List;

public interface IConfigRepository {

    /**
     * Set if device is master, slave otherwise.
     * @param value Is master device
     */
    void setIsMasterModule(boolean value);

    /**
     * @return Is device master
     */
    boolean getIsMasterModule();

    /**
     * Set master IP address.
     * Used for slave devices
     * @param address IP address for master device
     */
    void setMasterIP(InetAddress address);

    /**
     * @return Master InetAddress
     */
    InetAddress getMasterIP();

    /**
     * Set if device has video capability.
     * This value is important for master device.
     * @param value Has video capability
     */
    void setHasVideoCapability(boolean value);

    /**
     * @return Is device video capable
     */
    boolean getHasVideoCapability();

    void setOldVersion(boolean val);

    boolean isOldVersion();

    void addDevice(String ip);

    List<String> getDevices();

    void removeDevice(String ip);

}
