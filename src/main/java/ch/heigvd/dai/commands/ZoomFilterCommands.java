package ch.heigvd.dai.commands;

import ch.heigvd.dai.BMPClass.BMPFile;
import ch.heigvd.dai.filters.BMPZoomFilter;
import ch.heigvd.dai.ios.ZoomFilterInterface;

import java.io.IOException;
import java.util.concurrent.Callable;

import ch.heigvd.dai.ios.bmp.BMPReader;
import picocli.CommandLine;

@CommandLine.Command(name = "zoom", description = "Apply a zoom filter with an adjustable origin and ratio.")
public class ZoomFilterCommands implements Callable<Integer> {
    @CommandLine.ParentCommand protected Root parent;

    @CommandLine.Option(
            names = {"-r", "--ratio"},
            description = "Zoom ratio (required)",
            required = true)
    protected int ratio;

    @CommandLine.Option(
            names = {"-x", "--xOrigin"},
            description = "X coordinate of the new origin (default = 0)",
            defaultValue = "0")
    protected int xOrigin;

    @CommandLine.Option(
            names = {"-y", "--yOrigin"},
            description = "Y coordinate of the new origin (default = 0)",
            defaultValue = "0")
    protected int yOrigin;

    @Override
    public Integer call() throws IOException {
        ZoomFilterInterface zoomFilter = new BMPZoomFilter();

        BMPReader reader = new BMPReader();
        BMPFile bmpFile = reader.BMPReader(parent.getInputFilename());

        // Zoom in the middle if the default value is used
        if (yOrigin == 0) {
            yOrigin = bmpFile.getHeight() / 2;  // Centre vertical
        }
        if (xOrigin == 0) {
            xOrigin = bmpFile.getWidth() / 2;   // Centre horizontal
        }

        System.out.println(
                "Reading from " + parent.getInputFilename()
                        + " applying zoom with ratio " + ratio
                        + " from origin (" + xOrigin + ", " + yOrigin + ")"
                        + " writing in " + parent.getOutputFilename());


        zoomFilter.write(parent.getInputFilename(), parent.getOutputFilename(), ratio, yOrigin, xOrigin);

        return 0;
    }
}
