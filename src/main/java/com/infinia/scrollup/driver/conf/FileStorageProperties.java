package com.infinia.scrollup.driver.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String temporaryDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getTemporaryDir() {
        return temporaryDir;
    }

    public void setTemporaryDir(String temporaryDir) {
        this.temporaryDir = temporaryDir;
    }
}
