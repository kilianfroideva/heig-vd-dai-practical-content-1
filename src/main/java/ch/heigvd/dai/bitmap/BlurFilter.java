package ch.heigvd.dai.bitmap;

import java.io.IOException;

public class BlurFilter {
    class position {
        int x;
        int y;
    }

    double distance(position x1, position x2, double distance_metric) {
        return Math.pow(Math.pow(Math.abs(x1.x-x1.y),distance_metric)+Math.pow(Math.abs(x2.x-x2.y),distance_metric),1.0/distance_metric);
    }

    Pixel pixel_blur_filter(int y, int x, BMPFile original_bmp_file, double distance_metric, double weight, double radius){
        Pixel pixel_blur_filter = new Pixel();
        for(int idx=0; idx<original_bmp_file.width*original_bmp_file.height; idx++){
            int height = idx/original_bmp_file.width;
            int width = idx%original_bmp_file.width;

        }

        return pixel_blur_filter;
    }

    public void write(String inputFilePath, String outputFilePath, double distance_metric, double weight, double radius) {
        try {
            BMPReader reader = new BMPReader();
            BMPFile original_bmpFile = reader.BMPReader(inputFilePath);

            BMPFile blurred_bmpFile = new BMPFile();
            blurred_bmpFile = original_bmpFile;

            for(int idx=0; idx<original_bmpFile.width*original_bmpFile.height; idx++) {
                int y = idx/original_bmpFile.width;;
                int x = idx%original_bmpFile.width;
                blurred_bmpFile.pixels[y][x] = pixel_blur_filter(y,x,original_bmpFile, distance_metric, weight, radius);
            }

            // Step 3: Write the modified pixel data to a new BMP file
            BMPWriter writer = new BMPWriter();
            writer.write(blurred_bmpFile, outputFilePath);

            System.out.println("Conversion successful!");
            System.out.println("Blurred image saved as: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred during the conversion process:");
            e.printStackTrace();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Invalid BMP file format:");
            e.printStackTrace();
        }
    }
}