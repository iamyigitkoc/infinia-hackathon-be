package com.infinia.scrollup.driver.request;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.Data;

import java.net.InetAddress;

@Data
public class SetDeviceTypeRequest {

    private Boolean isMaster;

    private String masterDeviceIP;

}
