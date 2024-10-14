package ch.heigvd.dai.bitmap;

class Pixel {
    int red;
    int green;
    int blue;

    public void setRGB(int rgb){
        red = (rgb >> 16) & 0xFF;
        green = (rgb >> 8) & 0xFF;
        blue = rgb & 0xFF;
    }
    public int getRGB(){
        return (red << 16) | (green << 8) | blue;
    }
}

public class BMPFile {
    int height;
    int width;
    Pixel[][] pixels;
}