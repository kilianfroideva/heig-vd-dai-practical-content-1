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
    public void write(String inputFilePath, String outputFilePath, int ratio, int heightOrigin, int widthOrigin){
        try {
            BMPReader reader = new BMPReader();
            BMPFile bmpFile = reader.BMPReader(inputFilePath);

            int newHeight = bmpFile.getHeight()*ratio;
            int newWidth = bmpFile.getWidth()*ratio;

            Pixel[][] pixels = new Pixel[newHeight][newWidth];
            for(int y=0;y<newHeight;y++) {
                for (int x = 0; x < newWidth; x++) {
                    int originalY = heightOrigin + (y / ratio) - (newHeight / (2 * ratio));
                    int originalX = widthOrigin + (x / ratio) - (newWidth / (2 * ratio));

                    // Verification if coordinate are outside the origin bitmap
                    if (originalY >= 0 && originalY < bmpFile.getHeight() && originalX >= 0 && originalX < bmpFile.getWidth()) {
                        // Copy the original pixel
                        pixels[y][x] = bmpFile.getPixels()[originalY][originalX];
                    } else {
                        // Default color when outside the bitmap
                        pixels[y][x] = new Pixel(255, 255, 255);
                    }
                }
            }
            bmpFile.setPixels(pixels);

            BMPWriter writer = new BMPWriter();
            writer.write(bmpFile, outputFilePath);

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
