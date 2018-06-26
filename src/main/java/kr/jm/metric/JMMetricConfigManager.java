package kr.jm.metric;

import kr.jm.metric.config.AbstractConfigManager;
import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.helper.JMJson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * The type Metric properties manager.
 */
@Slf4j
@Getter
public class JMMetricConfigManager {
    private static final String INPUT_CONFIG_FILENAME = "InputConfig.json";
    private static final String MUTATING_CONFIG_FILENAME =
            "MutatingConfig.json";
    private static final String OUTPUT_CONFIG_FILENAME = "OutputConfig.json";

    private InputConfigManager inputConfigManager;
    private MutatingConfigManager mutatingConfigManager;
    private OutputConfigManager outputConfigManager;

    public JMMetricConfigManager() {
        this(INPUT_CONFIG_FILENAME, MUTATING_CONFIG_FILENAME,
                OUTPUT_CONFIG_FILENAME);
    }

    public JMMetricConfigManager(String inputConfigFileName, String
            mutatingConfigFileName, String outputConfigFileName) {
        this.inputConfigManager = new InputConfigManager(inputConfigFileName);
        this.mutatingConfigManager =
                new MutatingConfigManager(mutatingConfigFileName);
        this.outputConfigManager =
                new OutputConfigManager(outputConfigFileName);
    }

    public Map<String, InputConfigInterface> getInputConfigMap() {
        return inputConfigManager.getConfigMap();
    }

    public InputConfigInterface getInputConfig(String inputConfigId) {
        return inputConfigManager.getConfig(inputConfigId);
    }

    public InputConfigInterface removeInputConfig(String inputConfigId) {
        return inputConfigManager.removeConfig(inputConfigId);
    }

    public JMMetricConfigManager insertInputConfig(
            InputConfigInterface inputConfig) {
        inputConfigManager.insertConfig(inputConfig);
        return this;
    }

    public AbstractConfigManager<InputConfigInterface> insertConfigList(
            List<InputConfigInterface> configList) {
        return inputConfigManager.insertConfigList(configList);
    }

    public JMMetricConfigManager insertMutatingConfigList(
            List<MutatingConfig> configList) {
        mutatingConfigManager.insertConfigList(configList);
        return this;
    }

    public MutatingConfig getMutatingConfig(String mutatingConfigId) {
        return mutatingConfigManager.getConfig(mutatingConfigId);
    }

    public JMMetricConfigManager insertMutatingConfig(
            MutatingConfig mutatingConfig) {
        mutatingConfigManager.insertConfig(mutatingConfig);
        return this;
    }

    public Map<String, MutatingConfig> getMutatingConfigMap() {
        return mutatingConfigManager.getConfigMap();
    }

    public MutatingConfig removeMutatingConfig(String mutatingConfigId) {
        return mutatingConfigManager.removeConfig(mutatingConfigId);
    }

    public JMMetricConfigManager insertOutputConfigList(
            List<OutputConfigInterface> outputConfigList) {
        outputConfigManager.insertConfigList(outputConfigList);
        return this;
    }

    public JMMetricConfigManager insertOutputConfig(
            OutputConfigInterface inputOutputConfig) {
        outputConfigManager.insertConfig(inputOutputConfig);
        return this;
    }

    public Map<String, OutputConfigInterface> getOutputConfigMap() {
        return outputConfigManager.getConfigMap();
    }

    public OutputConfigInterface getOutputConfig(
            String outputConfigId) {
        return outputConfigManager.getConfig(outputConfigId);
    }

    public OutputConfigInterface removeOutputConfig(String outputConfigId) {
        return outputConfigManager.removeConfig(outputConfigId);
    }

    public void printAllConfig() {
        logAndStdOut("==== Input Config Map ====", inputConfigManager);
        logAndStdOut("==== Mutating Config Map ====", mutatingConfigManager);
        logAndStdOut("==== Output Config Map ====", outputConfigManager);
    }

    private void logAndStdOut(String infoHead,
            AbstractConfigManager configManager) {
        log.info(infoHead);
        System.out.println(infoHead);
        String info = JMJson.toPrettyJsonString(configManager.getConfigMap());
        log.info(info);
        System.out.println(info);
    }
}
