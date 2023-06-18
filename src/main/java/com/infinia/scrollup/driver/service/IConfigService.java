package com.infinia.scrollup.driver.service;

import com.infinia.scrollup.driver.enums.EnumDeviceType;
import com.infinia.scrollup.driver.repository.IConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.util.List;

public interface IConfigService {

    void changeDeviceType(EnumDeviceType type);

    void changeDeviceMasterIP(InetAddress address);

    void changeToOldVersion();

    void changeToNewVersion();

    boolean isOldVersion();

    List<String> getDevices();

    void addDevice(String ip);

    void removeDevice(String ip);

}
