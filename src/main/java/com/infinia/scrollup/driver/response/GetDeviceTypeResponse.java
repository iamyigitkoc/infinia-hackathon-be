package com.infinia.scrollup.driver.response;

import lombok.Data;

@Data
public class GetDeviceTypeResponse {
    public boolean isMaster = false;
    public String masterIP = "";
}
