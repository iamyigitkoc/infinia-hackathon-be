package com.infinia.scrollup.cropping.enums;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum EnumFileType {
    JPEG, MP4, UNSUPPORTED;

    public static EnumFileType getEnumByFile(Path file){
        String filename = file.getFileName().toString();
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        if(ext.equalsIgnoreCase("jpg")){
            return EnumFileType.JPEG;
        } else if (ext.equalsIgnoreCase("mp4")) {
            return EnumFileType.MP4;
        }
        return EnumFileType.UNSUPPORTED;
    }
}
