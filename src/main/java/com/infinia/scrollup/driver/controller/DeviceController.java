package com.infinia.scrollup.driver.controller;

import com.infinia.scrollup.driver.client.MediaViewerClient;
import com.infinia.scrollup.driver.response.ScrollUpClientResponse;
import com.infinia.scrollup.driver.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/device")
public class DeviceController {

    @Autowired
    IConfigService service;

    @Autowired
    private MediaViewerClient mediaViewerClient;

    @GetMapping(value = "/list")
    public ResponseEntity<List<ScrollUpClientResponse>> listDevices() {
        List<String> ips = this.service.getDevices();
        List<ScrollUpClientResponse> scrollUpClientResponseList = new ArrayList<>();
        ips.forEach(ip -> {
            scrollUpClientResponseList.add(this.mediaViewerClient.init(ip));
        });
        return new ResponseEntity<>(scrollUpClientResponseList, HttpStatus.OK);
    }

    @GetMapping(value = "/sync-all")
    public  ResponseEntity<String> syncAll(){
        List<String> ips = this.service.getDevices();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i<ips.size(); i++){
            //this.mediaViewerClient.playFrom(ips[i], orders[i] > 0 ? orders[i] - 1 : 0 );
            final int index = i;
            threads.add(new Thread(() -> {
                this.mediaViewerClient.playFrom(ips.get(index), 0 );
            }));
            threads.get(i).start();
        }

        while(!threads.isEmpty()){
            try {
                threads.get(threads.size() - 1).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            threads.remove(threads.get(threads.size() - 1));
        }
        return new ResponseEntity<>("Synced.", HttpStatus.OK);
    }
}
