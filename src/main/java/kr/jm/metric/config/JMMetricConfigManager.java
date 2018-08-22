package kr.jm.metric.config;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * The type Jm metric config manager.
 */
@Slf4j
public class JMMetricConfigManager {

    private static final String INPUT_CONFIG_FILENAME = "Input.json";
    private static final String MUTATING_CONFIG_FILENAME =
            "Mutator.json";
    private static final String OUTPUT_CONFIG_FILENAME = "Output.json";
    private static final String RUNNING_CONFIG_FILENAME =
            "JMMetricConfig.json";

    private InputConfigManager inputConfigManager;
    private MutatorConfigManager mutatorConfigManager;
    private OutputConfigManager outputConfigManager;
    private RunningConfigManager runningConfigManager;

    /**
     * Instantiates a new Jm metric config manager.
     */
    public JMMetricConfigManager() {
        this(RUNNING_CONFIG_FILENAME);
    }

    public JMMetricConfigManager(String runningConfigFilename) {
        this(INPUT_CONFIG_FILENAME, MUTATING_CONFIG_FILENAME,
                OUTPUT_CONFIG_FILENAME, runningConfigFilename);
    }

    /**
     * Instantiates a new Jm metric config manager.
     *
     * @param inputConfigFileName   the input config file name
     * @param mutatorConfigFileName the mutator config file name
     * @param outputConfigFileName  the output config file name
     * @param runningConfigFilename
     */
    public JMMetricConfigManager(String inputConfigFileName, String
            mutatorConfigFileName, String outputConfigFileName,
            String runningConfigFilename) {
        this.inputConfigManager =
                new InputConfigManager(inputConfigFileName);
        this.mutatorConfigManager =
                new MutatorConfigManager(mutatorConfigFileName);
        this.outputConfigManager =
                new OutputConfigManager(outputConfigFileName);
        this.runningConfigManager =
                new RunningConfigManager(runningConfigFilename,
                        this.inputConfigManager, this.mutatorConfigManager,
                        this.outputConfigManager);
    }

    /**
     * Gets input config map.
     *
     * @return the input config map
     */
    public Map<String, InputConfigInterface> getInputConfigMap() {
        return inputConfigManager.getConfigMap();
    }

    /**
     * Gets input config.
     *
     * @param inputConfigId the input config id
     * @return the input config
     */
    public InputConfigInterface getInputConfig(String inputConfigId) {
        return inputConfigManager.getConfig(inputConfigId);
    }

    /**
     * Remove input config input config interface.
     *
     * @param inputConfigId the input config id
     * @return the input config interface
     */
    public InputConfigInterface removeInputConfig(String inputConfigId) {
        return inputConfigManager.removeConfig(inputConfigId);
    }

    /**
     * Insert input config jm metric config manager.
     *
     * @param inputConfig the input config
     * @return the jm metric config manager
     */
    public JMMetricConfigManager insertInputConfig(
            InputConfigInterface inputConfig) {
        inputConfigManager.insertConfig(inputConfig);
        return this;
    }

    /**
     * Insert config list abstract config manager.
     *
     * @param configList the config list
     * @return the abstract config manager
     */
    public AbstractListConfigManager insertConfigList(
            List<InputConfigInterface> configList) {
        return inputConfigManager.insertConfigList(configList);
    }

    /**
     * Insert mutator config list jm metric config manager.
     *
     * @param configList the config list
     * @return the jm metric config manager
     */
    public JMMetricConfigManager insertMutatorConfigList(
            List<MutatorConfigInterface> configList) {
        mutatorConfigManager.insertConfigList(configList);
        return this;
    }

    /**
     * Gets mutator config.
     *
     * @param mutatorConfigId the mutator config id
     * @return the mutator config
     */
    public MutatorConfigInterface getMutatorConfig(String mutatorConfigId) {
        return mutatorConfigManager.getConfig(mutatorConfigId);
    }

    /**
     * Insert mutator config jm metric config manager.
     *
     * @param mutatorConfig the mutator config
     * @return the jm metric config manager
     */
    public JMMetricConfigManager insertMutatorConfig(
            MutatorConfigInterface mutatorConfig) {
        mutatorConfigManager.insertConfig(mutatorConfig);
        return this;
    }

    /**
     * Gets mutator config map.
     *
     * @return the mutator config map
     */
    public Map<String, MutatorConfigInterface> getMutatorConfigMap() {
        return mutatorConfigManager.getConfigMap();
    }

    /**
     * Remove mutator config mutator config interface.
     *
     * @param mutatorConfigId the mutator config id
     * @return the mutator config interface
     */
    public MutatorConfigInterface removeMutatorConfig(String
            mutatorConfigId) {
        return mutatorConfigManager.removeConfig(mutatorConfigId);
    }

    /**
     * Insert output config list jm metric config manager.
     *
     * @param outputConfigList the output config list
     * @return the jm metric config manager
     */
    public JMMetricConfigManager insertOutputConfigList(
            List<OutputConfigInterface> outputConfigList) {
        outputConfigManager.insertConfigList(outputConfigList);
        return this;
    }

    /**
     * Insert output config jm metric config manager.
     *
     * @param inputOutputConfig the input output config
     * @return the jm metric config manager
     */
    public JMMetricConfigManager insertOutputConfig(
            OutputConfigInterface inputOutputConfig) {
        outputConfigManager.insertConfig(inputOutputConfig);
        return this;
    }

    /**
     * Gets output config map.
     *
     * @return the output config map
     */
    public Map<String, OutputConfigInterface> getOutputConfigMap() {
        return outputConfigManager.getConfigMap();
    }

    /**
     * Gets output config.
     *
     * @param outputConfigId the output config id
     * @return the output config
     */
    public OutputConfigInterface getOutputConfig(
            String outputConfigId) {
        return outputConfigManager.getConfig(outputConfigId);
    }

    /**
     * Remove output config output config interface.
     *
     * @param outputConfigId the output config id
     * @return the output config interface
     */
    public OutputConfigInterface removeOutputConfig(String outputConfigId) {
        return outputConfigManager.removeConfig(outputConfigId);
    }

    /**
     * Print all config jm metric config manager.
     *
     * @return the jm metric config manager
     */
    public JMMetricConfigManager printAllConfig() {
        loggingConfigInfo("==== Input Config Map ====", inputConfigManager);
        loggingConfigInfo("==== Mutator Config Map ====",
                mutatorConfigManager);
        loggingConfigInfo("==== Output Config Map ====",
                outputConfigManager);
        return this;
    }

    private void loggingConfigInfo(String infoHead,
            AbstractListConfigManager configManager) {
        log.info(JMString.LINE_SEPARATOR + infoHead +
                JMString.LINE_SEPARATOR +
                JMJson.toPrettyJsonString(configManager.getConfigMap()));
    }

    public InputConfigInterface getInputConfig() {
        return runningConfigManager.getInputConfig();
    }

    public MutatorConfigInterface getMutatorConfig() {
        return runningConfigManager.getMutatorConfig();
    }

    public OutputConfigInterface getOutputConfig() {
        return runningConfigManager.getOutputConfig();
    }
}
