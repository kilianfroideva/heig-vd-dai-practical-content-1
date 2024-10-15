package ch.heigvd.dai.bitmap;

import java.io.IOException;

public class BlurFilter {
    class Position {
        int x;
        int y;
    }

    class PixelDouble {
        double red;
        double green;
        double blue;
    }

    double distance(Position p1, Position p2, double distance_metric) {
        if(distance_metric != 0) {
            // Circlish (Minkowski distance)
            return Math.pow(
                    Math.pow(Math.abs(p1.x - p2.x), distance_metric) +
                            Math.pow(Math.abs(p1.y - p2.y), distance_metric),
                    1.0 / distance_metric
            );
        } else {
            // Square (Chebyshev distance)
            return Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
        }
    }


    Pixel applyPixelBlurFilter(Position position, BMPFile original_bmp_file, double distance_metric, double weight, double radius){
        Pixel pixel_blur_filter = new Pixel();
        double weight_sum = 0.0;
        PixelDouble pixel_sum = new PixelDouble();
        pixel_sum.red = 0.0;
        pixel_sum.green = 0.0;
        pixel_sum.blue = 0.0;

        //Could be optimized if incremently check from the starting position
        for (int y = 0; y < original_bmp_file.height; y++) {
            for (int x = 0; x < original_bmp_file.width; x++) {
                Position position_read = new Position();
                position_read.x = x;
                position_read.y = y;
                double dist = distance(position, position_read, distance_metric);
                if(dist <= radius && dist>0){
                    double inv_dist = 1.0/distance(position,position_read,weight);
                    pixel_sum.red += original_bmp_file.pixels[y][x].red*inv_dist;
                    pixel_sum.green += original_bmp_file.pixels[y][x].green*inv_dist;
                    pixel_sum.blue += original_bmp_file.pixels[y][x].blue*inv_dist;
                    weight_sum += inv_dist;
                }
            }
        }

        if(weight_sum>0) {
            pixel_blur_filter.red = (int) Math.round(pixel_sum.red / weight_sum);
            pixel_blur_filter.green = (int) Math.round(pixel_sum.green / weight_sum);
            pixel_blur_filter.blue = (int) Math.round(pixel_sum.blue / weight_sum);
        }

        return pixel_blur_filter;
    }

    public void write(String inputFilePath, String outputFilePath, double distance_metric, double weight, double radius) {
        try {
            BMPReader reader = new BMPReader();
            BMPFile original_bmpFile = reader.BMPReader(inputFilePath);

            BMPFile blurred_bmpFile = original_bmpFile.clone();

            for (int y = 0; y < original_bmpFile.height; y++) {
                for (int x = 0; x < original_bmpFile.width; x++) {
                    Position position = new Position();
                    position.x = x;
                    position.y = y;
                    blurred_bmpFile.pixels[y][x] = applyPixelBlurFilter(position,original_bmpFile, distance_metric, weight, radius);
                }
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