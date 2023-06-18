package com.infinia.scrollup.driver.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

    String storeFile(MultipartFile file);

}
