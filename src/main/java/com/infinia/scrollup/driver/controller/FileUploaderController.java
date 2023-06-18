package com.infinia.scrollup.driver.controller;

import com.infinia.scrollup.driver.client.MediaViewerClient;
import com.infinia.scrollup.driver.conf.FileStorageProperties;
import com.infinia.scrollup.driver.exception.FileStorageException;
import com.infinia.scrollup.driver.service.IConfigService;
import com.infinia.scrollup.driver.service.ICroppingService;
import com.infinia.scrollup.driver.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/api/file")
public class FileUploaderController {

    private static String[] FILE_TYPES = {"image/jpeg", "video/mp4"};

    @Autowired
    private IConfigService config;

    @Autowired
    private MediaViewerClient mediaViewerClient;

    @Autowired
    private IFileStorageService service;

    @Autowired
    private ICroppingService croppingService;

    private Path temporaryDir;

    public FileUploaderController(FileStorageProperties properties){
        this.temporaryDir = Paths.get(properties.getTemporaryDir()).toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.temporaryDir);
        }catch (Exception e){
            throw new FileStorageException("Temporary directory is not accessible.");
        }
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws InterruptedException {
        String type = file.getContentType();
        if(Arrays.asList(FILE_TYPES).contains(type)){
            String upload = this.service.storeFile(file);

            String[] ips = this.config.getDevices().toArray(new String[0]);
            croppingService.scale(Paths.get(upload), ips.length);

            int[] orders = new int[ips.length];

            for (int i = 0; i < ips.length; i++) {
                Path uploaded = Paths.get(upload);
                String filename = uploaded.getFileName().toString().substring(0, uploaded.getFileName().toString().indexOf('.'));
                File uploadThis = temporaryDir.resolve(filename + "-crop-"+i+".mp4").toAbsolutePath().normalize().toFile();

                orders[i] = this.mediaViewerClient.sendChannelVideo(uploadThis, i, ips[i]);
            }

           List<Thread> threads = new ArrayList<>();
            for (int i = 0; i<ips.length; i++){
                //this.mediaViewerClient.playFrom(ips[i], orders[i] > 0 ? orders[i] - 1 : 0 );
                final int index = i;
                threads.add(new Thread(() -> {
                    this.mediaViewerClient.playFrom(ips[index], orders[index] > 0 ? orders[index] - 1 : 0 );
                }));
                threads.get(i).start();
            }

            while(!threads.isEmpty()){
                threads.get(threads.size() - 1).join();
                threads.remove(threads.get(threads.size() - 1));
            }

            return new ResponseEntity<>(upload, HttpStatus.OK);
        }
        return new ResponseEntity<>("Unsupported media type.", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

}
