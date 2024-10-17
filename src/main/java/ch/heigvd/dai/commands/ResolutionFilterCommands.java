package ch.heigvd.dai.commands;

import ch.heigvd.dai.filters.BMPResolutionFilter;
import ch.heigvd.dai.ios.ResolutionFilterInterface;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "resolution", description = "Apply a smaller resolution filter.")
public class ResolutionFilterCommands implements Callable<Integer> {
    @CommandLine.ParentCommand protected Root parent;

    @CommandLine.Option(
            names = {"-r", "--ratio"},
            description = "Resolution ratio (must be between 0 and 100, required)",
            required = true,
            converter = RatioConverter.class,
            order = 0)
    protected int ratio;

    @Override
    public Integer call() throws IOException {
        ResolutionFilterInterface resolutionFilter = new BMPResolutionFilter();

        System.out.println(
                "Reading from " + parent.getInputFilename()
                        + " applying smaller resolution with ratio " + ratio
                        + " writing in " + parent.getOutputFilename());

        resolutionFilter.write(parent.getInputFilename(), parent.getOutputFilename(), ratio);
        return 0;
    }

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
