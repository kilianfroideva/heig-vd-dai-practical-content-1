package ch.heigvd.dai.ios.bmp;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.heigvd.dai.BMPClass.BMPFile;

public class BMPWriter {

    public void write(BMPFile bmpFile , String filePath) throws IOException {
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            // Calculate row size with padding
            int rowSize = (bmpFile.getWidth() * 3 + 3) & ~3; // Align to 4 bytes
            int padding = rowSize - (bmpFile.getWidth() * 3);
            int pixelDataSize = rowSize * bmpFile.getHeight();
            int fileSize = 14 + 40 + pixelDataSize;

            // Write File Header (14 bytes)
            fos.write(new byte[] { 'B', 'M' }); // Signature
            fos.write(intToByteArray(fileSize)); // File size
            fos.write(shortToByteArray((short) 0)); // Reserved1
            fos.write(shortToByteArray((short) 0)); // Reserved2
            fos.write(intToByteArray(14 + 40)); // Pixel data offset

            // Write DIB Header (40 bytes)
            fos.write(intToByteArray(40)); // DIB header size
            fos.write(intToByteArray(bmpFile.getWidth())); // Image width
            fos.write(intToByteArray(bmpFile.getHeight())); // Image height
            fos.write(shortToByteArray((short) 1)); // Color planes
            fos.write(shortToByteArray((short) 24)); // Bits per pixel
            fos.write(intToByteArray(0)); // Compression (0 = none)
            fos.write(intToByteArray(pixelDataSize)); // Image size
            fos.write(intToByteArray(2835)); // Horizontal resolution (pixels/meter)
            fos.write(intToByteArray(2835)); // Vertical resolution (pixels/meter)
            fos.write(intToByteArray(0)); // Number of colors in palette
            fos.write(intToByteArray(0)); // Important colors

            // Write Pixel Data
            byte[] paddingBytes = new byte[padding];
            for (int y = bmpFile.getHeight() - 1; y >= 0; y--) { // BMP stores pixels bottom-to-top
                for (int x = 0; x < bmpFile.getWidth(); x++) {
                    byte blue = (byte)bmpFile.getPixels()[y][x].getBlue();
                    byte green = (byte)bmpFile.getPixels()[y][x].getGreen();
                    byte red = (byte)bmpFile.getPixels()[y][x].getRed();
                    fos.write(new byte[] { blue, green, red });
                }
                fos.write(paddingBytes); // Add padding
            }
        }
    }

    // Helper method to convert int to 4-byte array (little endian)
    private byte[] intToByteArray(int value) {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }

    // Helper method to convert short to 2-byte array (little endian)
    private byte[] shortToByteArray(short value) {
        return new byte[] {
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF)
        };
    }
}
