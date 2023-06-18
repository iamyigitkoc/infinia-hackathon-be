package com.infinia.scrollup.driver.repository;

import com.infinia.scrollup.driver.model.ConfigModel;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryConfigRepository implements IConfigRepository {

    private ConfigModel configModel = new ConfigModel();

    @Override
    public void setIsMasterModule(boolean value) {
        configModel.setMasterDevice(value);
    }

    @Override
    public boolean getIsMasterModule() {
        return configModel.isMasterDevice();
    }

    @Override
    public void setMasterIP(InetAddress address) {
        configModel.setMasterIP(address);
    }

    @Override
    public InetAddress getMasterIP() {
        return configModel.getMasterIP();
    }

    @Override
    public void setHasVideoCapability(boolean value) {
        configModel.setVideoCapable(value);
    }

    @Override
    public boolean getHasVideoCapability() {
        return configModel.isVideoCapable();
    }

    @Override
    public void setOldVersion(boolean val) {
        this.configModel.setOldVersion(val);
    }

    @Override
    public boolean isOldVersion() {
        return this.configModel.isOldVersion();
    }

    @Override
    public void addDevice(String ip) {
        this.configModel.getDevices().add(ip);
    }

    @Override
    public List<String> getDevices() {
        return this.configModel.getDevices();
    }

    public void removeDevice(String ip){
        this.configModel.setDevices(this.configModel.getDevices().stream().filter(oip -> !oip.equals(ip)).collect(Collectors.toList()));
    }
}
