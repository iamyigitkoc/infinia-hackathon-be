package com.infinia.scrollup.driver.service;

import com.infinia.scrollup.driver.conf.FileStorageProperties;
import com.infinia.scrollup.driver.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StaticFileStorageService implements IFileStorageService{

    private final Path storageLocation;

    public StaticFileStorageService(FileStorageProperties properties){
        this.storageLocation = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.storageLocation);
        }catch (Exception e){
            throw new FileStorageException("Upload directory is not accessible.");
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(UUID.randomUUID().toString());
        try{
            if(filename.contains("..")){
                throw new FileStorageException("Invalid filename.");
            }
            Path target = this.storageLocation.resolve(filename + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        }catch(IOException e){
            throw new FileStorageException("Couldn't upload file.", e);
        }
    }
}
