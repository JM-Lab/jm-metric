package kr.jm.metric.config;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.mutator.MutatorConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import kr.jm.utils.JMArrays;
import kr.jm.utils.JMOptional;
import kr.jm.utils.JMResources;
import kr.jm.utils.JMString;
import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMPath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class JMMetricConfigManager {

    public static final String CONFIG_DIR =
            Optional.ofNullable(JMResources.getSystemProperty("jm.metric.configDir")).orElse("config");

    private static final String INPUT_CONFIG_FILENAME = "Input.json";
    private static final String MUTATING_CONFIG_FILENAME = "Mutator.json";
    private static final String OUTPUT_CONFIG_FILENAME = "Output.json";
    private static final String RUNNING_CONFIG_FILENAME = "JMMetricConfig.json";

    @Getter
    private final InputConfigManager inputConfigManager;
    @Getter
    private final MutatorConfigManager mutatorConfigManager;
    @Getter
    private final OutputConfigManager outputConfigManager;
    @Getter
    private final RunningConfigManager runningConfigManager;
    @Getter
    private final String runningConfigFilePath;

    public JMMetricConfigManager() {
        this(RUNNING_CONFIG_FILENAME);
    }

    public JMMetricConfigManager(String runningConfigFilePath) {
        this(INPUT_CONFIG_FILENAME, MUTATING_CONFIG_FILENAME, OUTPUT_CONFIG_FILENAME, runningConfigFilePath);
    }

    /**
     * Instantiates a new Jm metric config manager.
     *
     * @param inputConfigFilePath   the input config file path
     * @param mutatorConfigFilePath the mutator config file path
     * @param outputConfigFilePath  the output config file path
     * @param runningConfigFilePath the running config file path
     */
    public JMMetricConfigManager(String inputConfigFilePath, String mutatorConfigFilePath, String outputConfigFilePath,
            String runningConfigFilePath) {
        this.inputConfigManager =
                new InputConfigManager(buildConfigFilePath(inputConfigFilePath, INPUT_CONFIG_FILENAME));
        this.mutatorConfigManager =
                new MutatorConfigManager(buildConfigFilePath(mutatorConfigFilePath, MUTATING_CONFIG_FILENAME));
        this.outputConfigManager =
                new OutputConfigManager(buildConfigFilePath(outputConfigFilePath, OUTPUT_CONFIG_FILENAME));
        this.runningConfigFilePath = runningConfigFilePath;
        this.runningConfigManager =
                new RunningConfigManager(runningConfigFilePath, this.inputConfigManager, this.mutatorConfigManager,
                        this.outputConfigManager);
    }

    protected String buildConfigFilePath(String path, String alternativePath) {
        return JMOptional.getOptional(path).map(JMPath.getInstance()::getPath).filter(JMPath.getInstance()::exists)
                .map(Path::toAbsolutePath)
                .map(Object::toString).orElseGet(() -> buildDefaultConfigPath(alternativePath));
    }

    private String buildDefaultConfigPath(String configFilePath) {
        return CONFIG_DIR + OS.getFileSeparator() + configFilePath;
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

    public JMMetricConfigManager insertInputConfig(InputConfigInterface inputConfig) {
        inputConfigManager.insertConfig(inputConfig);
        return this;
    }

    public JMMetricConfigManager insertMutatorConfigList(List<MutatorConfigInterface> configList) {
        mutatorConfigManager.insertConfigList(configList);
        return this;
    }

    public MutatorConfigInterface getMutatorConfig(String mutatorConfigId) {
        return mutatorConfigManager.getConfig(mutatorConfigId);
    }

    public JMMetricConfigManager insertMutatorConfig(
            MutatorConfigInterface mutatorConfig) {
        mutatorConfigManager.insertConfig(mutatorConfig);
        return this;
    }

    public Map<String, MutatorConfigInterface> getMutatorConfigMap() {
        return mutatorConfigManager.getConfigMap();
    }

    public MutatorConfigInterface removeMutatorConfig(String mutatorConfigId) {
        return mutatorConfigManager.removeConfig(mutatorConfigId);
    }

    public JMMetricConfigManager insertOutputConfigList(List<OutputConfigInterface> outputConfigList) {
        outputConfigManager.insertConfigList(outputConfigList);
        return this;
    }

    public JMMetricConfigManager insertOutputConfig(OutputConfigInterface inputOutputConfig) {
        outputConfigManager.insertConfig(inputOutputConfig);
        return this;
    }

    public Map<String, OutputConfigInterface> getOutputConfigMap() {
        return outputConfigManager.getConfigMap();
    }

    public OutputConfigInterface getOutputConfig(String outputConfigId) {
        return outputConfigManager.getConfig(outputConfigId);
    }

    public OutputConfigInterface removeOutputConfig(String outputConfigId) {
        return outputConfigManager.removeConfig(outputConfigId);
    }

    public JMMetricConfigManager printAllConfig() {
        loggingConfigInfo("Input", inputConfigManager);
        loggingConfigInfo("Mutator", mutatorConfigManager);
        loggingConfigInfo("Output", outputConfigManager);
        return this;
    }

    protected void loggingConfigInfo(String title, AbstractListConfigManager<ConfigInterface> configManager) {
        loggingConfigInfo(title, configManager.getConfigMap());
    }

    protected void loggingConfigInfo(String title, Object object) {
        log.info(buildInfo("==== " + title + " Config ====", JMJson.getInstance().toPrettyJsonString(object)));
    }

    private String buildInfo(String infoHead, String info) {
        return kr.jm.utils.JMString.LINE_SEPARATOR + infoHead + kr.jm.utils.JMString.LINE_SEPARATOR + info;
    }

    public String getInputConfigId() {
        return getConfigId(BindingConfig::getInputId);
    }

    private String getConfigId(Function<BindingConfig, String> configIdFunction) {
        return getBindingConfigOptional().map(configIdFunction).orElse(JMString.EMPTY);
    }

    public String getMutatorConfigId() {
        return getConfigId(BindingConfig::getMutatorId);
    }

    public String[] getOutputConfigIds() {
        return getBindingConfigOptional().map(BindingConfig::getOutputIds).orElse(JMArrays.EMPTY_STRINGS);
    }

    private Optional<BindingConfig> getBindingConfigOptional() {
        return Optional.ofNullable(this.runningConfigManager.getBindingConfig());
    }
}
