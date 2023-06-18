package com.infinia.scrollup.driver.service;

import com.infinia.scrollup.cropping.processor.CroppingProcessor;
import com.infinia.scrollup.driver.conf.FileStorageProperties;
import com.infinia.scrollup.driver.exception.FileStorageException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CroppingService implements ICroppingService {

    private final Path temporaryLocation;

    public CroppingService(FileStorageProperties properties){
        this.temporaryLocation = Paths.get(properties.getTemporaryDir()).toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.temporaryLocation);
        }catch (Exception e){
            throw new FileStorageException("Upload directory is not accessible.");
        }
    }

    @Override
    public void scale(Path sourceFile, int screenCount) {
        CroppingProcessor proc = new CroppingProcessor(sourceFile, this.temporaryLocation, screenCount);
        proc.run();

        try {
            proc.join();
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(CroppingService.class).error(e.getMessage());
        }
    }
}
