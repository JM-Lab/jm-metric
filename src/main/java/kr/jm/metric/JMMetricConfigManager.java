package kr.jm.metric;

import kr.jm.metric.config.input.InputConfigInterface;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        this.inputConfigManager = new InputConfigManager(INPUT_CONFIG_FILENAME);
        this.mutatingConfigManager = new MutatingConfigManager
                (MUTATING_CONFIG_FILENAME);
        this.outputConfigManager = new OutputConfigManager
                (OUTPUT_CONFIG_FILENAME);
    }

    public MutatingConfigManager bindDataIdToConfigId(String dataId,
            String configId) {
        return this.mutatingConfigManager
                .bindDataIdToConfigId(dataId, configId);
    }

    public List<MutatingConfig> getConfigListWithDataId(
            String dataId) {
        return this.mutatingConfigManager.getConfigListWithDataId(dataId);
    }

    public List<String> getConfigIdList(String dataId) {
        return this.mutatingConfigManager.getConfigIdList(dataId);
    }

    public MutatingConfig getMutatingConfig(
            String configId) {
        return this.mutatingConfigManager.getMutatingConfig(configId);
    }

    public MutatingConfigManager removeDataId(String dataId) {
        return this.mutatingConfigManager.removeDataId(dataId);
    }

    public MutatingConfigManager insertConfig(
            MutatingConfig mutatingConfig) {
        return this.mutatingConfigManager.insertConfig(mutatingConfig);
    }

    public Map<String, Set<String>> getDataIdConfigIdSetMap() {
        return this.mutatingConfigManager.getDataIdConfigIdSetMap();
    }

    public Map<String, InputConfigInterface> getInputConfigMap() {
        return this.inputConfigManager.getConfigMap();
    }

    public Map<String, MutatingConfig> getMutatingConfigMap() {
        return this.mutatingConfigManager.getConfigMap();
    }

    public Map<String, OutputConfigInterface> getOutputConfigMap() {
        return this.outputConfigManager.getConfigMap();
    }
}
