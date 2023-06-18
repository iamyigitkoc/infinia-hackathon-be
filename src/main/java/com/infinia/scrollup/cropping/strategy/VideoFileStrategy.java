package com.infinia.scrollup.cropping.strategy;

import com.infinia.scrollup.cropping.listener.ProgressListener;
import org.slf4j.LoggerFactory;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingArgument;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filters.CropFilter;
import ws.schild.jave.filters.ScaleFilter;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class VideoFileStrategy extends AbstractFileStrategy{

    private static int MAX_TIME = 10000;

    public VideoFileStrategy(int totalWidth, int maxHeight, int screenCount, Path sourceFile, Path targetDir) {
        super(totalWidth, maxHeight, screenCount, sourceFile, targetDir);
    }


    @Override
    public void crop() throws IOException {
        File source = this.getSourceFile().toFile();
        scale(source);
        divide(source);
    }
    
    public void scale(File source){
        File targetFile = this.getTargetDir().resolve(source.getName().substring(0, source.getName().indexOf('.')) + "-scaled.mp4").toAbsolutePath().normalize().toFile();
        ProgressListener listener = new ProgressListener();
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(64000);
        audio.setChannels(1);
        audio.setSamplingRate(22050);

        VideoAttributes video = new VideoAttributes();
        video.setBitRate(160000);
        video.setFrameRate(24);
        video.setSize(new VideoSize(this.getTotalWidth(), this.getMaxHeight()));

        ScaleFilter scale = new ScaleFilter(new VideoSize(this.getTotalWidth(), this.getMaxHeight()));
        video.addFilter(scale);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), targetFile, attrs, listener);
            long timeLeft = MAX_TIME;
            long lastMillis = System.currentTimeMillis();
            while (!listener.isFinished() && timeLeft > 0){
                long current = System.currentTimeMillis();
                long timeDiff = current - lastMillis;
                timeLeft = timeLeft - timeDiff;
                lastMillis = current;
            }
        } catch (EncoderException e) {
            LoggerFactory.getLogger(VideoFileStrategy.class).error(e.getMessage());
        }
    }
    
    public void divide(File source){
        File[] targets = new File[this.getScreenCount()];
        for (int i = 0; i < targets.length; i++) {
            targets[i] = this.getTargetDir().resolve(source.getName().substring(0, source.getName().indexOf('.')) + "-crop-"+i+".mp4").toAbsolutePath().normalize().toFile();
            ProgressListener listener = new ProgressListener();
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(64000);
            audio.setChannels(1);
            audio.setSamplingRate(22050);

            int videoW = this.getTotalWidth() / this.getScreenCount();

            VideoAttributes video = new VideoAttributes();
            video.setBitRate(160000);
            video.setFrameRate(24);
            video.setSize(new VideoSize(videoW, this.getMaxHeight()));

            CropFilter cropFilter = new CropFilter(videoW, this.getMaxHeight(), videoW*i, 0);
            video.addFilter(cropFilter);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            Encoder encoder = new Encoder();
            try {
                encoder.encode(new MultimediaObject(source), targets[i], attrs, listener);
                long timeLeft = MAX_TIME;
                long lastMillis = System.currentTimeMillis();
                while (!listener.isFinished() && timeLeft > 0){
                    long current = System.currentTimeMillis();
                    long timeDiff = current - lastMillis;
                    timeLeft = timeLeft - timeDiff;
                    lastMillis = current;
                }
            } catch (EncoderException e) {
                LoggerFactory.getLogger(VideoFileStrategy.class).error(e.getMessage());
            }
        }
    }
}
