package ch.heigvd.dai.commands;

import ch.heigvd.dai.filters.BMPBlurFilter;
import ch.heigvd.dai.ios.BlurFilterInterface;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "blurFilter", description = "Apply a blur filter.")
public class BlurFilterCommands implements Callable<Integer> {
    @CommandLine.ParentCommand protected Root parent;

    @CommandLine.Option(
            names = {"-r", "--radius"},
            description = "Set the radius (must be positive)",
            required = true,
            converter = PositiveDoubleConverter.class, // Use custom converter for validation
            order = 0)
    protected double radius;

    @CommandLine.Option(
            names = {"-d", "--distanceMetric"},
            description = "Set the metric distance",
            required = false,
            defaultValue = "2",
            order = 1)
    protected double distance_metric = 2;

    @CommandLine.Option(
            names = {"-w", "--weight"},
            description = "Set the weight",
            required = false,
            defaultValue = "0",
            order = 2)
    protected double weight = 0;

    @Override
    public Integer call() {
        BlurFilterInterface blurFilter = new BMPBlurFilter();

        System.out.println(
                "Reading from "
                        + parent.getInputFilename()
                        + " writing in "
                        + parent.getOutputFilename());

        blurFilter.write(parent.getInputFilename(), parent.getOutputFilename(), distance_metric, weight, radius);
        return 0;
    }

    // Custom converter to ensure positive double values
    public static class PositiveDoubleConverter implements CommandLine.ITypeConverter<Double> {
        @Override
        public Double convert(String value) throws Exception {
            double parsedValue = Double.parseDouble(value);
            if (parsedValue <= 0) {
                throw new CommandLine.TypeConversionException("Radius must be a positive number.");
            }
            return parsedValue;
        }
    }
}
