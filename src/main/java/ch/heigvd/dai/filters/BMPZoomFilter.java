package ch.heigvd.dai.filters;

import ch.heigvd.dai.BMPClass.BMPFile;
import ch.heigvd.dai.BMPClass.Pixel;
import ch.heigvd.dai.ios.ZoomFilterInterface;
import ch.heigvd.dai.ios.bmp.BMPReader;
import ch.heigvd.dai.ios.bmp.BMPWriter;

import java.io.IOException;

//The zoom reset the origin (up right) of the bitmap and rescale the bitmap
public class BMPZoomFilter implements ZoomFilterInterface {
    @Override
    public void write(String inputFilePath, String outputFilePath, int ratio_percentage, int heightRatioPercentage, int widthRatioPercentage){
        try {
            BMPReader reader = new BMPReader();
            BMPFile bmpFile = reader.BMPReader(inputFilePath);

            double ratio = (double)(ratio_percentage)/100.0;
            double heightRatio = (double)(heightRatioPercentage)/100.0;
            double widthRatio = (double)(widthRatioPercentage)/100.0;

            int newHeight = (int) Math.round(bmpFile.getHeight() * ratio);
            int newWidth = (int) Math.round(bmpFile.getWidth() * ratio);

            int centerY = (int) Math.round(bmpFile.getHeight() * heightRatio);
            int centerX = (int) Math.round(bmpFile.getWidth() * widthRatio);

            BMPFile zoomed_bmpFile = new BMPFile(newWidth,newHeight);

            // For each pixel in the zoomed image
            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < newWidth; x++) {

                    // Map the zoomed image pixel back to the original image
                    // The shift is based on how far we are from the center
                    int originalY = centerY + (int) Math.round((y - newHeight / 2));
                    int originalX = centerX + (int) Math.round((x - newWidth / 2));

                    // Ensure the coordinates are within bounds of the original image
                    originalY = Math.max(0, Math.min(originalY, bmpFile.getHeight() - 1));
                    originalX = Math.max(0, Math.min(originalX, bmpFile.getWidth() - 1));

                    // Assign the pixel from the original image to the zoomed image
                    zoomed_bmpFile.getPixels()[y][x] = new Pixel();
                    zoomed_bmpFile.getPixels()[y][x].setPixel(bmpFile.getPixels()[originalY][originalX]);
                }
            }

            BMPWriter writer = new BMPWriter();
            writer.write(zoomed_bmpFile, outputFilePath);

            System.out.println("Conversion successful!");
            System.out.println("Zoomed image saved as: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred during the conversion process:");
            e.printStackTrace();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Invalid BMP file format:");
            e.printStackTrace();
        }

    }
}
