package kr.jm.metric;

import kr.jm.metric.config.JMMetricConfigManager;
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
    private JMMetricConfigManager jmMetricConfigManager;
    private String inputId;
    private String mutatorId;
    private String[] outputIds;

    /**
     * Main.
     *
     * @param args the args
     */
    public void main(String... args) {
        Optional.ofNullable(parseCLI(buildCLIOptions(), args))
                .ifPresent(commandLine -> {
                    applyCommandLine(commandLine);
                    this.jmMetric = new JMMetric(this.jmMetricConfigManager,
                            inputId, mutatorId, outputIds);
                    this.jmMetric.getJmMetricConfigManager().printAllConfig();
                    JMThread.runAsync(this.jmMetric::start);
                    OS.addShutdownHook(this.jmMetric::close);
                });
    }

    private CommandLine parseCLI(Options options, String... args) {
        try {
            return initArgs(options, new DefaultParser().parse(options, args,
                    true));
        } catch (Exception e) {
            JMExceptionManager
                    .handleException(log, e, "parseCLI", Arrays.toString(args));
            printHelp(options);
            return null;
        }
    }

    private CommandLine initArgs(Options options, CommandLine commandLine) {
        return commandLine.hasOption("help") ? printHelp(options) : commandLine;
    }

    private CommandLine printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "JMMetric";
        final String usageHeader = "Options:";
        final String usageFooter =
                "See https://github.com/JM-Lab/jm-metric for further details.";
        formatter.printHelp(120, syntax, usageHeader, options, usageFooter,
                true);
        return null;
    }

    private Options buildCLIOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print help message");
        options.addOption("c", "config", true,
                "a file path of JMMetricConfig");
        options.addOption("i", "inputId", true,
                "a inputId, default: Stdin");
        options.addOption("m", "mutatorId", true,
                "a mutatorId, default: Raw");
        options.addOption("o", "outputIds", true,
                "outputIds as CSV, default: Stdout");
        return options;
    }

    private void applyCommandLine(CommandLine commandLine) {
        Optional.ofNullable(commandLine.getOptionValue("config"))
                .map(config -> this.jmMetricConfigManager =
                        new JMMetricConfigManager(config))
                .ifPresent(jmMetricConfigManager -> {
                    this.inputId = jmMetricConfigManager.getInputConfigId();
                    this.mutatorId = jmMetricConfigManager.getMutatorConfigId();
                    this.outputIds = jmMetricConfigManager.getOutputConfigIds();
                });
        Optional.ofNullable(commandLine.getOptionValue("inputId"))
                .ifPresent(inputId -> this.inputId = inputId);
        Optional.ofNullable(commandLine.getOptionValue("mutatorId"))
                .ifPresent(mutatorId -> this.mutatorId = mutatorId);
        Optional.ofNullable(commandLine.getOptionValue("outputIds"))
                .map(JMArrays::buildArrayFromCsv)
                .ifPresent(outputIds -> this.outputIds = outputIds);
    }

}
