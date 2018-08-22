package kr.jm.metric;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMThread;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.Optional;

/**
 * The type Jm metric main.
 */
@Getter
@Slf4j
public class JMMetricMain {

    private JMMetric jmMetric;
    private String inputId;
    private String mutatorId;
    private String[] outputIds;

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        if (parseCLI(buildCLIOptions(), args)) {
            this.jmMetric = new JMMetric(inputId, mutatorId, outputIds);
            this.jmMetric.getJmMetricConfigManager().printAllConfig();
            JMThread.runAsync(this.jmMetric::start);
            OS.addShutdownHook(this.jmMetric::close);
        }
    }

    private boolean parseCLI(Options options, String... args) {
        try {
            return initArgs(options, new DefaultParser().parse(options, args,
                    true));
        } catch (Exception e) {
            JMExceptionManager
                    .logException(log, e, "parseCLI", Arrays.toString(args));
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
        this.mutatorId = line.getOptionValue("mutatorId");
        Optional.ofNullable(line.getOptionValue("outputIds"))
                .map(JMArrays::buildArrayFromCsv)
                .ifPresent(outputIds -> this.outputIds = outputIds);
    }

    private Options buildCLIOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print help message");
        options.addOption("i", "inputId", true,
                "from a inputId in Input.json, default: -i StdIn");
        options.addRequiredOption("m", "mutatorId", true,
                "from a mutatorId in Mutator.json, *Required*");
        options.addOption("o", "outputIds", true,
                "array as CSV from outputIds in Output.json, default: -o StdOut");
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
