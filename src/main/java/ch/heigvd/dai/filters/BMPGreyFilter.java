package ch.heigvd.dai.filters;

import ch.heigvd.dai.ios.bmp.BMPReader;
import ch.heigvd.dai.ios.bmp.BMPWriter;
import ch.heigvd.dai.BMPClass.BMPFile;
import ch.heigvd.dai.ios.GreyFilterInterface;

import java.io.IOException;

public class BMPGreyFilter implements GreyFilterInterface {

    @Override
    public void write(String inputFilePath, String outputFilePath, boolean inverted) {
        try {
            BMPReader reader = new BMPReader();
            BMPFile bmpFile = reader.BMPReader(inputFilePath);

            // Step 2: Convert each pixel to black or white
            for (int y = 0; y < bmpFile.getHeight(); y++) {
                for (int x = 0; x < bmpFile.getWidth(); x++) {
                    double luminance =
                            (double)bmpFile.getPixels()[y][x].getRed()/3.0 +
                            (double)bmpFile.getPixels()[y][x].getGreen()/3.0 +
                                    (double)bmpFile.getPixels()[y][x].getBlue()/3.0;

                    // Calculate grayscale value (ensure it's within 0-255)
                    int gray = (int)Math.round(luminance);

                    gray = Math.min(255, Math.max(0, gray));
                    if(inverted) {
                        gray = 255 - gray;
                    }

                    bmpFile.getPixels()[y][x].setRed(gray);
                    bmpFile.getPixels()[y][x].setGreen(gray);
                    bmpFile.getPixels()[y][x].setBlue(gray);
                }
            }

            // Step 3: Write the modified pixel data to a new BMP file
            BMPWriter writer = new BMPWriter();
            writer.write(bmpFile, outputFilePath);

            System.out.println("Conversion successful!");
            System.out.println("Grey image saved as: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred during the conversion process:");
            e.printStackTrace();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Invalid BMP file format:");
            e.printStackTrace();
        }
    }
}
