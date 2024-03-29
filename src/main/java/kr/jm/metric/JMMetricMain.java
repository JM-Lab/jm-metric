package kr.jm.metric;

import kr.jm.metric.config.JMMetricConfigManager;
import kr.jm.utils.JMArrays;
import kr.jm.utils.JMThread;
import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMException;
import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Optional;

@Getter
public class JMMetricMain {

    protected Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private CommandLine commandLine;
    private JMMetric jmMetric;
    private JMMetricConfigManager jmMetricConfigManager;
    private String inputId;
    private String mutatorId;
    private String[] outputIds;

    public void start(String... args) {
        Optional.ofNullable(parseCLI(buildCLIOptions(), args))
                .ifPresent(commandLine -> {
                    applyCommandLine(commandLine);
                    runHookBeforeStart(
                            this.jmMetric = new JMMetric(this.jmMetricConfigManager, inputId, mutatorId, outputIds));
                    JMThread.runAsync(this.jmMetric::start);
                    OS.addShutdownHook(this::runHookAfterShutdown);
                });
    }

    protected void runHookAfterShutdown() {
        this.jmMetric.close();
    }

    protected void runHookBeforeStart(JMMetric jmMetric) {
        jmMetric.getJmMetricConfigManager().printAllConfig();
    }

    private CommandLine parseCLI(Options options, String... args) {
        try {
            return initArgs(options, new DefaultParser().parse(options, args, true));
        } catch (Exception e) {
            JMException.handleException(log, e, "parseCLI", Arrays.toString(args));
            printHelp(options);
            return null;
        }
    }

    private CommandLine initArgs(Options options, CommandLine commandLine) {
        return commandLine.hasOption("help") ? printHelp(options) : commandLine;
    }

    private CommandLine printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        String syntax = "JMMetric";
        String usageHeader = "Options:";
        String usageFooter = "See https://github.com/JM-Lab/jm-metric for further details.";
        formatter.printHelp(120, syntax, usageHeader, options, usageFooter, true);
        return null;
    }

    protected Options buildCLIOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print help message");
        options.addOption("c", "config", true, "a file path of JMMetricConfig");
        options.addOption("i", "inputId", true, "a inputId, default: Stdin");
        options.addOption("m", "mutatorId", true, "a mutatorId, default: Raw");
        options.addOption("o", "outputIds", true, "outputIds as CSV, default: Stdout");
        return options;
    }

    private void applyCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
        extractConfigValueOptional("config").map(config -> this.jmMetricConfigManager = new JMMetricConfigManager(config))
                .ifPresent(jmMetricConfigManager -> {
                    this.inputId = jmMetricConfigManager.getInputConfigId();
                    this.mutatorId = jmMetricConfigManager.getMutatorConfigId();
                    this.outputIds = jmMetricConfigManager.getOutputConfigIds();
                });
        extractConfigValueOptional("inputId").ifPresent(inputId -> this.inputId = inputId);
        extractConfigValueOptional("mutatorId").ifPresent(mutatorId -> this.mutatorId = mutatorId);
        extractConfigValueOptional("outputIds").map(JMArrays::buildArrayFromCsv)
                .ifPresent(outputIds -> this.outputIds = outputIds);
    }

    public Optional<String> extractConfigValueOptional(String opt) {
        return Optional.ofNullable(this.commandLine.getOptionValue(opt));
    }

}
