package kr.jm.metric;

import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.config.output.OutputConfigManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

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

    public MutatingConfigManager bindInputIdToMutatingConfigId(
            String inputId, String mutatingConfigId) {
        return mutatingConfigManager
                .bindInputIdToMutatingConfigId(inputId, mutatingConfigId);
    }

    public Optional<MutatingConfig> getMutatingConfigAsOpt(String inputId) {
        return mutatingConfigManager.getMutatingConfigAsOpt(inputId);
    }

    public Optional<String> getMutatingConfigIdAsOpt(String inputId) {
        return mutatingConfigManager.getMutatingConfigIdAsOpt(inputId);
    }

    public MutatingConfig getMutatingConfig(String configId) {
        return mutatingConfigManager.getMutatingConfig(configId);
    }

    public MutatingConfigManager removeInputId(String inputId) {
        return mutatingConfigManager.removeInputId(inputId);
    }

    public MutatingConfigManager insertConfig(
            MutatingConfig mutatingConfig) {
        return mutatingConfigManager.insertConfig(mutatingConfig);
    }

    public Map<String, String> getInputIdMutatingConfigIdMap() {
        return mutatingConfigManager.getInputIdMutatingConfigIdMap();
    }
}
