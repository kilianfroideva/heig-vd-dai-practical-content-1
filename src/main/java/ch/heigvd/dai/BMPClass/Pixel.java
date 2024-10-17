package ch.heigvd.dai.BMPClass;

public class Pixel {
    int red;
    int green;
    int blue;

    public Pixel() {}
    public Pixel(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public int getRed() {return red;}
    public int getGreen() {return green;}
    public int getBlue() {return blue;}

    public void setRed(int red) {this.red = red;}
    public void setGreen(int green) {this.green = green;}
    public void setBlue(int blue) {this.blue = blue;}

    public void setPixel(Pixel pixel){red=pixel.getRed();green=pixel.getGreen();blue=pixel.getBlue();}

    public void setRGB(int rgb){
        red = (rgb >> 16) & 0xFF;
        green = (rgb >> 8) & 0xFF;
        blue = rgb & 0xFF;
    }
    public int getRGB(){
        return (red << 16) | (green << 8) | blue;
    }
}