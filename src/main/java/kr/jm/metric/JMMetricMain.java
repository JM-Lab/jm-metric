package kr.jm.metric;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.util.Optional;

/**
 * The type Jm metric main.
 */
@Getter
@Slf4j
public class JMMetricMain {

    private JMMetric jmMetric;
    private String inputId;
    private String mutatingId;
    private String[] outputIds;

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        if (parseCLI(buildCLIOptions(), args)) {
            this.jmMetric = new JMMetric(inputId, mutatingId, outputIds);
            this.jmMetric.printAllConfig();
            this.jmMetric.start();
            OS.addShutdownHook(jmMetric::close);
        }
    }

    private boolean parseCLI(Options options, String... args) {
        try {
            return initArgs(options, new DefaultParser().parse(options, args,
                    true));
        } catch (Exception e) {
            JMExceptionManager.logException(log, e, "parseCLI", args);
            printHelp(options);
            return false;
        }
    }

    private boolean initArgs(Options options, CommandLine commandLine) {
        if (commandLine.hasOption("help")) {
            printHelp(options);
            return false;
        } else
            applyCommandLine(commandLine);
        return true;
    }

    private void applyCommandLine(CommandLine line) {
        Optional.ofNullable(line.getOptionValue("inputId"))
                .ifPresent(inputId -> this.inputId = inputId);
        this.mutatingId = line.getOptionValue("mutatingId");
        Optional.ofNullable(line.getOptionValue("outputIds"))
                .map(JMArrays::buildArrayFromCsv)
                .ifPresent(outputIds -> this.outputIds = outputIds);
    }

    private Options buildCLIOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print help message");
        options.addOption("i", "inputId", true,
                "from a inputId in InputConfig.json, default: -i StdIn");
        options.addRequiredOption("m", "mutatingId", true,
                "from a configId in MutatingConfig.json, *Required*");
        options.addOption("O", "outputIds", true,
                "array as CSV from outputIds in OutputConfig.json, default: -o StdOut");
        return options;
    }

    private void printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "JMMetric";
        final String usageHeader = "Options:";
        final String usageFooter =
                "See https://github.com/JM-Lab/jm-metric for further details.";
        formatter.printHelp(120, syntax, usageHeader, options, usageFooter,
                true);
    }
}
