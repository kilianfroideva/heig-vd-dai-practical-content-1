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

    public BMPFile(int width, int height) {
        setHeight(height);
        setWidth(width);
        setPixels(new Pixel[height][width]);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = new Pixel();
            }
        }
    }

}