package ch.heigvd.dai.ios;

public interface BlurFilterInterface {
    void write(String inputFilePath, String outputFilePath, double distance_metric, double weight, double radius);
}
