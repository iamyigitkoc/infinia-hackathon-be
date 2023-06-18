package com.infinia.scrollup.driver.model;

import lombok.Data;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigModel {

    boolean isMasterDevice = false;
    boolean isVideoCapable = true;
    boolean isOldVersion = true;
    InetAddress masterIP = null;
    List<String> devices = new ArrayList<>();

}
