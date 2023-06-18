package com.infinia.scrollup.cropping.strategy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ImageFileStrategy extends AbstractFileStrategy {

    public ImageFileStrategy(int totalWidth, int maxHeight, int screenCount, Path sourceFile, Path targetDir) {
        super(totalWidth, maxHeight, screenCount, sourceFile, targetDir);
    }

    @Override
    public void crop() throws IOException {
        BufferedImage image = ImageIO.read(this.getSourceFile().toFile());
        int imgHeight = image.getHeight(), imgWidth = image.getWidth();
        double ratioHeight = (double) getMaxHeight() / (double) imgHeight, ratioWidth = (double) getTotalWidth() / (double) imgWidth;
        double minRatio = Double.min(ratioHeight, ratioWidth);
        int scaledWidth = (int) (imgWidth * minRatio), scaledHeight = (int) (imgHeight * minRatio);
        BufferedImage scaledImage = new BufferedImage((int) (imgWidth * minRatio), (int) (imgHeight * minRatio), image.getType());

        Graphics2D scaleGraphic = scaledImage.createGraphics();
        scaleGraphic.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        scaleGraphic.dispose();
        String filename = getSourceFile().getFileName().toString().substring(0, getSourceFile().getFileName().toString().indexOf('.'));
        String scaledImagePath = String.valueOf(getTargetDir().resolve( filename + "-scaled.jpg").toAbsolutePath().normalize());
        ImageIO.write(scaledImage, "jpg", new File(scaledImagePath));


    }
}
