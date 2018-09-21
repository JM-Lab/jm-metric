package kr.jm.metric.config;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Jm metric config manager.
 */
@Slf4j
public class JMMetricConfigManager {

    public static final String CONFIG_DIR = Optional.ofNullable(
            JMResources.getSystemProperty("jm.metric.configDir"))
            .orElse("config");

    private static final String INPUT_CONFIG_FILENAME = "Input.json";
    private static final String MUTATING_CONFIG_FILENAME = "Mutator.json";
    private static final String OUTPUT_CONFIG_FILENAME = "Output.json";
    private static final String RUNNING_CONFIG_FILENAME = "JMMetricConfig.json";

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

    public JMMetricConfigManager(String runningConfigFilePath) {
        this(INPUT_CONFIG_FILENAME, MUTATING_CONFIG_FILENAME,
                OUTPUT_CONFIG_FILENAME, runningConfigFilePath);
    }

    /**
     * Instantiates a new Jm metric config manager.
     *
     * @param inputConfigFilePath   the input config file name
     * @param mutatorConfigFilePath the mutator config file name
     * @param outputConfigFilePath  the output config file name
     * @param runningConfigFilePath
     */
    public JMMetricConfigManager(String inputConfigFilePath, String
            mutatorConfigFilePath, String outputConfigFilePath,
            String runningConfigFilePath) {
        this.inputConfigManager = new InputConfigManager(
                buildConfigFilePath(inputConfigFilePath,
                        INPUT_CONFIG_FILENAME));
        this.mutatorConfigManager = new MutatorConfigManager(
                buildConfigFilePath(mutatorConfigFilePath,
                        MUTATING_CONFIG_FILENAME));
        this.outputConfigManager = new OutputConfigManager(
                buildConfigFilePath(outputConfigFilePath,
                        OUTPUT_CONFIG_FILENAME));
        this.runningConfigManager = new RunningConfigManager(
                buildConfigFilePath(runningConfigFilePath,
                        RUNNING_CONFIG_FILENAME), this.inputConfigManager,
                this.mutatorConfigManager, this.outputConfigManager);
    }

    private String buildConfigFilePath(String path, String alternativePath) {
        return JMOptional.getOptional(path).map(JMPath::getPath)
                .filter(JMPath::exists).map(Path::toAbsolutePath)
                .map(Object::toString)
                .orElseGet(() -> buildDefaultConfigPath(alternativePath));
    }

    private String buildDefaultConfigPath(String configFilePath) {
        return CONFIG_DIR + OS.getFileSeparator() + configFilePath;
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
        printInfo(buildInfo(infoHead,
                JMJson.toPrettyJsonString(configManager.getConfigMap())));
    }

    private void printInfo(String info) {
        log.info(info);
        System.out.println(info);
    }

    private String buildInfo(String infoHead, String info) {
        return JMString.LINE_SEPARATOR + infoHead + JMString.LINE_SEPARATOR +
                info;
    }

    public String getInputConfigId() {
        return Optional.ofNullable(runningConfigManager.getInputConfig())
                .map(InputConfigInterface::getInputId).orElse(JMString.EMPTY);
    }

    public String getMutatorConfigId() {
        return Optional.ofNullable(runningConfigManager.getMutatorConfig())
                .map(MutatorConfigInterface::getMutatorId)
                .orElse(JMString.EMPTY);
    }

    public String[] getOutputConfigIds() {
        return runningConfigManager.getOutputConfigs().stream()
                .map(OutputConfigInterface::getOutputId).toArray(String[]::new);
    }
}
