package ch.heigvd.dai.ios;
import java.io.*;

import ch.heigvd.dai.bitmap.BMP_file;

// Classe interne pour stocker les informations du BMP
class BMPInfo {
    int width;
    int height;
    int bitsPerPixel;

    BMPInfo(int width, int height, int bitsPerPixel) {
        this.width = width;
        this.height = height;
        this.bitsPerPixel = bitsPerPixel;
    }
}

//Pour bitmap de 24 bits
public abstract class BMPInput {
    public BMPInfo getInput(String filename){
        int width = -1;
        int height = -1;
        int bitsPerPixel = -1;
        try {
            InputStream file = new FileInputStream(filename);
            BufferedInputStream bFile = new BufferedInputStream(file);

            byte[] header = new byte[54];  // En-tête BMP de 54 octets
            bFile.read(header, 0, 54);

            // Largeur (octets 18-21)
            width = ((header[21] & 0xFF) << 24) | ((header[20] & 0xFF) << 16) |
                    ((header[19] & 0xFF) << 8) | (header[18] & 0xFF);

            // Hauteur (octets 22-25)
            height = ((header[25] & 0xFF) << 24) | ((header[24] & 0xFF) << 16) |
                    ((header[23] & 0xFF) << 8) | (header[22] & 0xFF);

            // Bits par pixel (octets 28-29)
            bitsPerPixel = ((header[29] & 0xFF) << 8) | (header[28] & 0xFF);


        }catch(IOException e){
        }
        return new BMPInfo(width, height, bitsPerPixel);

    }
}
