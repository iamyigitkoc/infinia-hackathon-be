package com.infinia.scrollup.cropping.processor;

import com.infinia.scrollup.cropping.enums.EnumFileType;
import com.infinia.scrollup.cropping.strategy.IFileStrategy;
import com.infinia.scrollup.cropping.strategy.ImageFileStrategy;
import com.infinia.scrollup.cropping.strategy.VideoFileStrategy;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class CroppingProcessor extends Thread {

    private final int screenCount;

    private final Path sourceFile;

    private final Path targetDir;



    private final int SCREEN_WIDTH = 192;

    private final int SCREEN_HEIGHT = 384;

    public CroppingProcessor(Path sourceFile, Path targetDir, int screenCount){
        this.screenCount = screenCount;
        this.sourceFile = sourceFile;
        this.targetDir = targetDir;
    }

    @Override
    public void run(){
        int totalWidth = SCREEN_WIDTH * this.screenCount;
        EnumFileType type = EnumFileType.getEnumByFile(sourceFile);
        IFileStrategy strategy;
        switch (type){
            case JPEG:
                strategy = new ImageFileStrategy(totalWidth, SCREEN_HEIGHT, this.screenCount, sourceFile, targetDir);
                break;
            case MP4:
                strategy = new VideoFileStrategy(totalWidth, SCREEN_HEIGHT, this.screenCount, sourceFile, targetDir);
                break;
            default:
                strategy = null;
        }

        try {
            strategy.crop();
        } catch (IOException e) {
            LoggerFactory.getLogger(CroppingProcessor.class).error("Unsuccessful to crop file: "+e.getMessage());
            return;
        }
    }

}
