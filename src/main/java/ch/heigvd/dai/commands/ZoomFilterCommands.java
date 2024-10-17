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
            names = {"-x", "--xRatio"},
            description = "X ratio from the original file in percentage (default = 50)",
            defaultValue = "50",
            required = false)
    protected int xOrigin = 50;

    @CommandLine.Option(
            names = {"-y", "--yRatio"},
            description = "Y ratio from the original file in percentage (default = 50)",
            defaultValue = "50",
            required = false)
    protected int yOrigin = 50;

    @Override
    public Integer call() throws IOException {
        ZoomFilterInterface zoomFilter = new BMPZoomFilter();

        System.out.println(
                "Reading from " + parent.getInputFilename()
                        + " applying zoom with ratio " + ratio
                        + " from origin (" + xOrigin + ", " + yOrigin + ")"
                        + " writing in " + parent.getOutputFilename());


        zoomFilter.write(parent.getInputFilename(), parent.getOutputFilename(), ratio, yOrigin, xOrigin);

        return 0;
    }
}
