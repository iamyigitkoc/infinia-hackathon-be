package com.infinia.scrollup.driver.service;

import com.infinia.scrollup.driver.enums.EnumDeviceType;
import com.infinia.scrollup.driver.repository.IConfigRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ConfigService implements IConfigService{

    @Autowired
    IConfigRepository repository;

    List<Process> currentProcesses;

    public ConfigService(){
        currentProcesses = new ArrayList<>();
    }

    @Override
    public void changeDeviceType(EnumDeviceType type) {
        switch (type){
            case SLAVE_DEVICE:
                this.repository.setIsMasterModule(false);
                break;
            case MASTER_DEVICE:
                this.repository.setIsMasterModule(true);
                break;
        }
    }

    @Override
    public void changeDeviceMasterIP(InetAddress address) {
        this.repository.setMasterIP(address);
    }

    @Override
    public void changeToOldVersion() {
        this.repository.setOldVersion(true);
    }

    @Override
    public void changeToNewVersion() {
        this.repository.setOldVersion(false);
    }


    @Override
    public boolean isOldVersion(){
        return this.repository.isOldVersion();
    }

    @Override
    public List<String> getDevices() {
        return this.repository.getDevices();
    }

    @Override
    public void addDevice(String ip) {
        this.repository.addDevice(ip);
    }

    @Override
    public void removeDevice(String ip) {
        this.repository.removeDevice(ip);
    }


}
