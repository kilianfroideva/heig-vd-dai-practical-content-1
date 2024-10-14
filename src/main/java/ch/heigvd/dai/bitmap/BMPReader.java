package ch.heigvd.dai.bitmap;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class BMPReader {
    public BMPFile BMPReader(String filePath) throws IOException {
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath))) {
            // Read File Header
            byte[] fileHeader = new byte[14];
            fis.read(fileHeader);

            // Verify the signature
            if (fileHeader[0] != 'B' || fileHeader[1] != 'M') {
                throw new IllegalArgumentException("Not a valid BMP file");
            }

            // Read DIB Header
            byte[] dibHeader = new byte[40];
            fis.read(dibHeader);

            // Extract image dimensions
            BMPFile bmpFile = new BMPFile();
            bmpFile.width = byteArrayToInt(dibHeader, 4);
            bmpFile.height = byteArrayToInt(dibHeader, 8);

            // Extract bits per pixel
            int bitsPerPixel = byteArrayToShort(dibHeader, 14);
            if (bitsPerPixel != 24) {
                throw new UnsupportedOperationException("Only 24-bit BMP files are supported");
            }

            // Extract pixel data offset
            int pixelDataOffset = byteArrayToInt(fileHeader, 10);

            // Skip to the pixel data offset
            byte[] skipData = new byte[pixelDataOffset - 14 - 40];
            fis.read(skipData) ;


            // Calculate padding
            int rowSize = (bmpFile.width * 3 + 3) & ~3; // Each row is padded to the nearest multiple of 4 bytes
            int padding = rowSize - (bmpFile.width * 3);

            // Initialize pixel array
            bmpFile.pixels = new Pixel[bmpFile.height][bmpFile.width];

            // Read pixel data
            byte[] row = new byte[rowSize];
            for (int y = bmpFile.height - 1; y >= 0; y--) { // BMP stores pixels bottom-to-top
                fis.read(row);
                int index = 0;
                for (int x = 0; x < bmpFile.width; x++) {
                    Pixel pixel = new Pixel();
                    pixel.blue = Byte.toUnsignedInt(row[index++]);
                    pixel.green = Byte.toUnsignedInt(row[index++]);
                    pixel.red = Byte.toUnsignedInt(row[index++]);
                    bmpFile.pixels[y][x] = pixel;
                }
                // Skip padding if any (already accounted for in rowSize)
            }
            return bmpFile;
        }
    }

    // Helper method to convert 4 bytes to int (little endian)
    private int byteArrayToInt(byte[] array, int start) {
        return (Byte.toUnsignedInt(array[start]) |
                (Byte.toUnsignedInt(array[start + 1]) << 8) |
                (Byte.toUnsignedInt(array[start + 2]) << 16) |
                (Byte.toUnsignedInt(array[start + 3]) << 24));
    }

    // Helper method to convert 2 bytes to short (little endian)
    private int byteArrayToShort(byte[] array, int start) {
        return Byte.toUnsignedInt(array[start]) |
                (Byte.toUnsignedInt(array[start + 1]) << 8);
    }
}
