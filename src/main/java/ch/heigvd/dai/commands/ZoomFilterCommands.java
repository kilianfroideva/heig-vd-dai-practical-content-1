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

    // Use converter to validate ratio is between 0 and 100
    @CommandLine.Option(
            names = {"-r", "--ratio"},
            description = "Zoom ratio (must be between 0 and 100, required)",
            required = true,
            converter = RatioConverter.class,
            order = 0)
    protected int ratio;

    @CommandLine.Option(
            names = {"-x", "--xRatio"},
            description = "X ratio from the original file in percentage (default = 50, must be between 0 and 100)",
            defaultValue = "50",
            required = false,
            converter = RatioConverter.class,
            order = 1)
    protected int xOrigin = 50;

    @CommandLine.Option(
            names = {"-y", "--yRatio"},
            description = "Y ratio from the original file in percentage (default = 50, must be between 0 and 100)",
            defaultValue = "50",
            required = false,
            converter = RatioConverter.class,
            order = 2)
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

    // Custom converter to ensure integer values between 0 and 100
    public static class RatioConverter implements CommandLine.ITypeConverter<Integer> {
        @Override
        public Integer convert(String value) throws Exception {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0 || parsedValue > 100) {
                throw new CommandLine.TypeConversionException(
                        String.format("Invalid value '%s': must be an integer between 0 and 100.", value));
            }
            return parsedValue;
        }
    }
}
