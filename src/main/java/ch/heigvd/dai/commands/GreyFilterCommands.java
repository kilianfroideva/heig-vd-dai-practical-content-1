package ch.heigvd.dai.commands;

import ch.heigvd.dai.filters.BMPGreyFilter;
import ch.heigvd.dai.ios.GreyFilterInterface;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "grey", description = "Apply a grey-scaling filter.", sortOptions = false)
public class GreyFilterCommands implements Callable<Integer> {
    @CommandLine.ParentCommand protected Root parent;

    @CommandLine.Option(
            names = {"-i", "--inverted"},
            description = "Invert black and white",
            required = false,
            defaultValue = "false")
    protected boolean inverted = false;

    @Override
    public Integer call() {
        GreyFilterInterface greyFilter = new BMPGreyFilter();

        System.out.println(
                "Reading from "
                        + parent.getInputFilename()
                        + " writing in "
                        + parent.getOutputFilename());

        greyFilter.write(parent.getInputFilename(),parent.getOutputFilename(),inverted);
        return 0;
    }
}
