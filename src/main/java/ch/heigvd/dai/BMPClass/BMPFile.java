package ch.heigvd.dai.BMPClass;

public class BMPFile {
    int height;
    int width;
    Pixel[][] pixels;

    public int getHeight() {return height;}
    public int getWidth() {return width;}
    public Pixel[][] getPixels() {return pixels;}

    public void setHeight(int height) {this.height = height;}
    public void setWidth(int width) {this.width = width;}
    public void setPixels(Pixel[][] pixels) {this.pixels = pixels;}
}