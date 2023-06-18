package com.infinia.scrollup.cropping.listener;

import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

public class ProgressListener implements EncoderProgressListener {
    private boolean isFinished = false;

    public ProgressListener() {
        //code
    }

    public void message(String m) {
        //code
    }

    public void progress(int p) {

        //Find %100 progress
        double progress = p / 1000.00;
        if(progress >= 100.0 && !isFinished){
            this.isFinished = true;
        }else{
            isFinished = false;
        }
    }

    public boolean isFinished(){
        return this.isFinished;
    }

    public void sourceInfo(MultimediaInfo m) {
        //code
    }
}
