package ch.heigvd.dai.commands;

import picocli.CommandLine;

@CommandLine.Command(
    description = "Apply filters to a 24-bit uncompressed bitmap file",
    version = "1.0.0",
    subcommands = {
      GreyFilterCommands.class,
    },
    scope = CommandLine.ScopeType.INHERIT,
    mixinStandardHelpOptions = true)
public class Root {

  @CommandLine.Parameters(index = "0", description = "The name of the input file.")
  protected String inputFilename;

  @CommandLine.Parameters(index = "1", description = "The name of the output file.")
  protected String outputFilename;

  public String getInputFilename() {
    return inputFilename;
  }
  public String getOutputFilename() { return outputFilename; }
}
