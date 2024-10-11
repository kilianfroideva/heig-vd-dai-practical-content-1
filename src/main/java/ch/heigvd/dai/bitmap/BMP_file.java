package ch.heigvd.dai.bitmap;

class Pixel {
    char red;
    char green;
    char blue;
}

class Header {
    int height;
    int width;
}

class Data {
    private Pixel[][] pixels;
}

public class BMP_file {
    Header header;
    Data data;
}