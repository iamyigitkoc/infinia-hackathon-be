package com.infinia.scrollup.driver.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinia.scrollup.cropping.enums.EnumFileType;
import com.infinia.scrollup.driver.exception.FileStorageException;
import com.infinia.scrollup.driver.response.ScrollUpClientResponse;
import com.infinia.scrollup.driver.response.UploadClientResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MediaViewerClient {

    RestTemplate restTemplate = new RestTemplate();
    String port = "4631";

    public int sendChannelVideo(File source, int order, String ip){
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("name", "filename");
        EnumFileType type = EnumFileType.getEnumByFile(Paths.get(source.toURI()));
        String contentType = "";
        if(type == EnumFileType.JPEG){
            contentType = "image/jpg";
        }else if(type == EnumFileType.MP4){
            contentType = "video/mp4";
        }else{
            throw new FileStorageException("Invalid type");
        }
        body.add("content-type", contentType);
        body.add("filename", new FileSystemResource(source));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://"+ip+":"+port+"/upload/"+source.getName(), requestEntity, String.class );
        ObjectMapper simpleMapper = new ObjectMapper();
        int count = 0;
        try {
            count = simpleMapper.readValue(response.getBody(), ScrollUpClientResponse.class).getCount();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public void playFrom(String ip, int order){
        Map<String, String> formData = new HashMap<>();
        formData.put("index", String.valueOf(Math.max(order, 0)));
        ResponseEntity<String> response = restTemplate.postForEntity("http://"+ip+":"+port+"/playFrom", formData, String.class );
    }

    public ScrollUpClientResponse init(String ip){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Object> request = new HttpEntity<>(null, headers);
        String content = restTemplate.postForEntity(getHost(ip)+"/init", request, String.class).getBody();
        ObjectMapper simpleMapper = new ObjectMapper();
        try {
            return simpleMapper.readValue(content, ScrollUpClientResponse.class) ;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private String getHost(String ip){
        return "http://"+ip+":"+port;
    }


}
