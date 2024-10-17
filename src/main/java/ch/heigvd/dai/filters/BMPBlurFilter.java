package ch.heigvd.dai.filters;

import ch.heigvd.dai.ios.bmp.BMPReader;
import ch.heigvd.dai.ios.bmp.BMPWriter;
import ch.heigvd.dai.BMPClass.*;
import ch.heigvd.dai.ios.BlurFilterInterface;

import java.io.IOException;

public class BMPBlurFilter implements BlurFilterInterface {
    public static class Position {
        int x;
        int y;
    }

    public static class PixelDouble {
        double red;
        double green;
        double blue;
    }

    public static double distance(Position p1, Position p2, double distance_metric) {
        if(distance_metric < 0){
            // Square
            return Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
        } else if(distance_metric == 0){
            // Constant weights
            return 1;
        } else {
            // Circlish (Minkowski distance)
            return Math.pow(
                    Math.pow(Math.abs(p1.x - p2.x), distance_metric) +
                            Math.pow(Math.abs(p1.y - p2.y), distance_metric),
                    1.0 / distance_metric
            );
        }
    }


    public static Pixel applyPixelBlurFilter(Position position, BMPFile original_bmp_file, double distance_metric, double weight, double radius){
        Pixel pixel_blur_filter = new Pixel();
        double weight_sum = 0.0;
        PixelDouble pixel_sum = new PixelDouble();
        pixel_sum.red = 0.0;
        pixel_sum.green = 0.0;
        pixel_sum.blue = 0.0;

        for (int y = Math.max(0,position.y - (int)radius); y <= Math.min(original_bmp_file.getHeight() - 1, position.y + (int)radius); y++) {
            for (int x = Math.max(0,position.x - (int)radius); x <= Math.min(original_bmp_file.getWidth() - 1, position.x + (int)radius); x++){
                Position position_read = new Position();
                position_read.x = x;
                position_read.y = y;
                double dist = distance(position, position_read, distance_metric);
                if(dist <= radius && dist>0){
                    double inv_dist = 1.0/distance(position,position_read,weight);
                    pixel_sum.red += original_bmp_file.getPixels()[y][x].getRed()*inv_dist;
                    pixel_sum.green += original_bmp_file.getPixels()[y][x].getGreen()*inv_dist;
                    pixel_sum.blue += original_bmp_file.getPixels()[y][x].getBlue()*inv_dist;
                    weight_sum += inv_dist;
                }
            }
        }

        if(weight_sum>0) {
            pixel_blur_filter.setRed((int) Math.round(pixel_sum.red / weight_sum));
            pixel_blur_filter.setGreen((int) Math.round(pixel_sum.green / weight_sum));
            pixel_blur_filter.setBlue((int) Math.round(pixel_sum.blue / weight_sum));
        }

        return pixel_blur_filter;
    }

    @Override
    public void write(String inputFilePath, String outputFilePath, double distance_metric, double weight, double radius) {
        try {
            BMPReader reader = new BMPReader();
            BMPFile original_bmpFile = reader.BMPReader(inputFilePath);

            BMPFile blurred_bmpFile = new BMPFile(original_bmpFile.getWidth(),original_bmpFile.getHeight());

            for (int y = 0; y < original_bmpFile.getHeight(); y++) {
                for (int x = 0; x < original_bmpFile.getWidth(); x++) {
                    Position position = new Position();
                    position.x = x;
                    position.y = y;

                    blurred_bmpFile.getPixels()[y][x].setPixel(applyPixelBlurFilter(position,original_bmpFile, distance_metric, weight, radius));
                }
            }

            // Step 3: Write the modified pixel data to a new BMP file
            BMPWriter writer = new BMPWriter();
            writer.write(blurred_bmpFile, outputFilePath);

            // Generate the filter visualization and save it as "<outputFilePath>_filter.bmp"
            String filterOutputFilePath = outputFilePath.replace(".bmp", "_filter.bmp");
            generateBlurFilterVisualization(filterOutputFilePath, radius, distance_metric, weight);

            // Output success messages
            System.out.println("Conversion successful!");
            System.out.println("Blurred image saved as: " + outputFilePath);
            System.out.println("Filter visualization saved as: " + filterOutputFilePath);

        } catch (IOException e) {
            System.err.println("An error occurred during the conversion process:");
            e.printStackTrace();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Invalid BMP file format:");
            e.printStackTrace();
        }
    }

    // Function to visualize the blur filter
    void generateBlurFilterVisualization(String outputFilePath,double radius, double distance_metric, double weight) {
        int resolution = 2*((int)Math.round(1.25*radius))+1;
        BMPFile visual_bmpFile = new BMPFile(resolution, resolution);

        Position center = new Position();
        center.x = resolution / 2;
        center.y = resolution / 2;

        Position next_to = new Position();
        next_to.x = center.x + 1;
        next_to.y = center.y + 1;

        double max_inv_dist = 1.0 / distance(center,next_to,weight);
        // Iterate over the bitmap and calculate pixel color based on distance and weight
        for (int y = 0; y < resolution; y++) {
            for (int x = 0; x < resolution; x++) {
                Position current = new Position();
                current.x = x;
                current.y = y;

                double dist = distance(center, current, distance_metric);

                Pixel pixel = new Pixel();
                if (dist == 0) {
                    // Center pixel is red
                    pixel.setRed(255);
                    pixel.setGreen(0);
                    pixel.setBlue(0);
                } else if (dist > radius) {
                    // Pixels outside the radius are white
                    pixel.setRed(0);
                    pixel.setGreen(0);
                    pixel.setBlue(0);
                } else {
                    // Pixels inside the radius are gray scaled based on weight
                    double inv_dist = 1.0 / distance(center,current,weight); // Inverse distance as weight
                    int gray_value = (int) Math.min(255, 255 * inv_dist/max_inv_dist); // Gray value based on weight
                    pixel.setRed(gray_value);
                    pixel.setGreen(gray_value);
                    pixel.setBlue(gray_value);
                }

                visual_bmpFile.getPixels()[y][x].setPixel(pixel);
            }
        }

        // Write the visual representation to a BMP file
        try {
            BMPWriter writer = new BMPWriter();
            writer.write(visual_bmpFile, outputFilePath);
            System.out.println("Visualization saved as: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}