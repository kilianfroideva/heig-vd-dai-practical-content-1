package ch.heigvd.dai.filters;

import ch.heigvd.dai.BMPClass.BMPFile;
import ch.heigvd.dai.BMPClass.Pixel;
import ch.heigvd.dai.ios.ResolutionFilterInterface;
import ch.heigvd.dai.ios.bmp.BMPReader;
import ch.heigvd.dai.ios.bmp.BMPWriter;
import ch.heigvd.dai.filters.BMPBlurFilter;

import java.io.IOException;

//The zoom reset the origin (up right) of the bitmap and rescale the bitmap
public class BMPResolutionFilter implements ResolutionFilterInterface {
    @Override
    public void write(String inputFilePath, String outputFilePath, int ratio_percentage){
        try {
            BMPReader reader = new BMPReader();
            BMPFile bmpFile = reader.BMPReader(inputFilePath);

            double ratio = (double)(ratio_percentage)/100.0;

            int newHeight = (int) Math.round(bmpFile.getHeight() * ratio);
            int newWidth = (int) Math.round(bmpFile.getWidth() * ratio);

            int centerY = (int) Math.round(bmpFile.getHeight() * 0.5);
            int centerX = (int) Math.round(bmpFile.getWidth() * 0.5);

            BMPFile resolution_bmpFile = new BMPFile(newWidth,newHeight);

            // For each pixel in the zoomed image
            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < newWidth; x++) {

                    int originalY = centerY + (int) Math.round((y - newHeight / 2) / ratio);
                    int originalX = centerX + (int) Math.round((x - newWidth / 2) / ratio);

                    BMPBlurFilter blurFilter = new BMPBlurFilter();
                    BMPBlurFilter.Position position = new BMPBlurFilter.Position();
                    position.x = originalX;
                    position.y = originalY;

                    resolution_bmpFile.getPixels()[y][x] = new Pixel();

                    //Apply blur effect around each pixel with square filter of good size and constant weight (mean average)
                    resolution_bmpFile.getPixels()[y][x].setPixel(BMPBlurFilter.applyPixelBlurFilter(position, bmpFile,-1,0,1.0/ratio));
                }
            }

            BMPWriter writer = new BMPWriter();
            writer.write(resolution_bmpFile, outputFilePath);

            System.out.println("Conversion successful!");
            System.out.println("Smaller resolution image saved as: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred during the conversion process:");
            e.printStackTrace();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Invalid BMP file format:");
            e.printStackTrace();
        }

    }
}
