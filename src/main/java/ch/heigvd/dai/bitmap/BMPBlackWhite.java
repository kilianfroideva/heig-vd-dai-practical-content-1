package ch.heigvd.dai.bitmap;

import java.io.IOException;

public class BMPBlackWhite {
    /**
     * Converts a BMP image to black and white.
     *
     * Usage:
     * java BMPBlackWhite <input_bmp_path> <output_bmp_path>
     *
     * Example:
     * java BMPBlackWhite image_input.bmp image_output.bmp
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java BMPBlackWhite <input_bmp_path> <output_bmp_path>");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];

        try {
            BMPReader reader = new BMPReader();
            BMPFile bmpFile = reader.BMPReader(inputFilePath);

            // Step 2: Convert each pixel to black or white
            for (int y = 0; y < bmpFile.height; y++) {
                for (int x = 0; x < bmpFile.width; x++) {
                    double luminance =
                            (double)bmpFile.pixels[y][x].red/3.0 +
                            (double)bmpFile.pixels[y][x].green/3.0 +
                                    (double)bmpFile.pixels[y][x].blue/3.0;

                    // Calculate grayscale value (ensure it's within 0-255)
                    int gray = (int)Math.round(luminance);

                    boolean inverted = false;
                    gray = Math.min(255, Math.max(0, gray));
                    if(inverted) {
                        gray = 255 - gray;
                    }

                    bmpFile.pixels[y][x].red = gray;
                    bmpFile.pixels[y][x].green = gray;
                    bmpFile.pixels[y][x].blue = gray;
                }
            }

            // Step 3: Write the modified pixel data to a new BMP file
            BMPWriter writer = new BMPWriter();
            writer.write(bmpFile, outputFilePath);

            System.out.println("Conversion successful!");
            System.out.println("Black and white image saved as: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred during the conversion process:");
            e.printStackTrace();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Invalid BMP file format:");
            e.printStackTrace();
        }
    }
}
