package com.infinia.scrollup.cropping.strategy;

import java.nio.file.Path;

public abstract class AbstractFileStrategy implements IFileStrategy{

    private final int totalWidth;

    private final int maxHeight;

    private final int screenCount;

    private final Path sourceFile;

    private final Path targetDir;

    public AbstractFileStrategy(int totalWidth, int maxHeight, int screenCount, Path sourceFile, Path targetDir){
        this.totalWidth = totalWidth;
        this.maxHeight = maxHeight;
        this.screenCount = screenCount;
        this.sourceFile = sourceFile;
        this.targetDir = targetDir;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public int getScreenCount() {
        return screenCount;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public Path getTargetDir() {
        return targetDir;
    }
}
