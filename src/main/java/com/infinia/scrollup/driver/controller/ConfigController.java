package com.infinia.scrollup.driver.controller;

import com.infinia.scrollup.driver.enums.EnumDeviceType;
import com.infinia.scrollup.driver.request.SetDeviceTypeRequest;
import com.infinia.scrollup.driver.request.SetVersionRequest;
import com.infinia.scrollup.driver.response.GetDeviceTypeResponse;
import com.infinia.scrollup.driver.service.IConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/config")
public class ConfigController {

    @Autowired
    IConfigService service;

    private boolean lockChange = false;

    @PostMapping(value = "/mode")
    public ResponseEntity<String> setDeviceType(@RequestBody SetDeviceTypeRequest request){
        if(lockChange){
            return new ResponseEntity<>("Unavailable to change mode.", HttpStatus.BAD_REQUEST);
        }
        lockChange = true;
        if(!request.getIsMaster()){
            try{
                InetAddress master = InetAddress.getByName(request.getMasterDeviceIP());
                this.service.changeDeviceType(EnumDeviceType.SLAVE_DEVICE);
                this.service.changeDeviceMasterIP(master);
            }catch(UnknownHostException e){
                return new ResponseEntity<>("Unknown host.", HttpStatus.BAD_REQUEST);
            }
        }else{
            this.service.changeDeviceType(EnumDeviceType.MASTER_DEVICE);
            this.service.changeDeviceMasterIP(null);
        }
        LoggerFactory.getLogger(ConfigController.class).info("Restarted all modules.");
        lockChange = false;
        return new ResponseEntity<>("Changed mode.", HttpStatus.OK);
    }

    @GetMapping(value = "/mode")
    public ResponseEntity<GetDeviceTypeResponse> getDeviceType(){
        GetDeviceTypeResponse response = new GetDeviceTypeResponse();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/version")
    public ResponseEntity<String> setVersion(@RequestBody SetVersionRequest request){
        if(request.isOldVersion()){
            this.service.changeToOldVersion();
            return new ResponseEntity<>("Changed to old version.", HttpStatus.OK);
        }
        this.service.changeToNewVersion();
        return new ResponseEntity<>("Changed to new version.", HttpStatus.OK);
    }

    @GetMapping(value = "/add-device/{ip}")
    public ResponseEntity<String> addDevice(@PathVariable(value = "ip") String ip){
        this.service.addDevice(ip);
        return new ResponseEntity("Added device", HttpStatus.OK);
    }

    @GetMapping(value = "/devices")
    public ResponseEntity<List<String>> listDevices(){
        return new ResponseEntity<>(this.service.getDevices(), HttpStatus.OK);
    }

    @GetMapping(value = "/remove-device/{ip}")
    public ResponseEntity<String> removeDevice(@PathVariable(value = "ip") String ip){
        this.service.removeDevice(ip);
        return new ResponseEntity<>("Removed device", HttpStatus.OK);
    }

}
