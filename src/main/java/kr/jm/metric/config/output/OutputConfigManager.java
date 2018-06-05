package kr.jm.metric.config.output;

import kr.jm.metric.config.ConfigManager;
import kr.jm.metric.output.OutputInterface;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OutputConfigManager {
    private static final String OUTPUT_CONFIG_FILENAME = "OutputConfig.json";
    private static final String OUTPUT_CONFIG_TYPE = "outputConfigType";
    private Set<String> outputConfigTypeSet;

    private Map<String, OutputConfigInterface> outputConfigMap;

    private Map<String, OutputInterface> outputMap;

    public OutputConfigManager() {
        this.outputConfigTypeSet = new HashSet<>(Arrays.stream(OutputConfigType
                .values()).map(Object::toString).collect(Collectors.toList()));
        this.outputConfigMap = new HashMap<>();
        this.outputMap = new HashMap<>();
        loadOutputConfig();
        JMLog.info(log, "OutputConfigManager",
                JMJson.toPrettyJsonString(outputConfigMap.values()));
    }

    private void loadOutputConfig() {
        ConfigManager.buildAllConfigMapList(OUTPUT_CONFIG_FILENAME)
                .stream().map(this::transform).filter(Objects::nonNull).forEach(
                outputConfig -> this.outputConfigMap
                        .put(outputConfig.getConfigId(), outputConfig));
    }

    private OutputConfigInterface transform(Map<String, Object> configMap) {
        return JMOptional.getOptional(configMap, OUTPUT_CONFIG_TYPE)
                .map(Object::toString)
                .filter(this.outputConfigTypeSet::contains)
                .map(OutputConfigType::valueOf)
                .map(OutputConfigType::getTypeReference)
                .map(typeReference -> JMJson
                        .transform(configMap, typeReference))
                .orElseGet(() -> JMExceptionManager
                        .handleExceptionAndReturnNull(log, JMExceptionManager
                                        .newRunTimeException(
                                                "No outputConfigType !!! - " + configMap
                                                        .get(OUTPUT_CONFIG_TYPE)),
                                "transform", configMap));
    }

    public OutputInterface getOutput(String configId) {
        return JMOptional.getOptional(this.outputConfigMap, configId)
                .map(outputConfig -> JMMap
                        .getOrPutGetNew(this.outputMap, configId,
                                outputConfig::buildOutput)).orElse(null);
    }

    public Map<String, OutputConfigInterface> getOutputConfigMap() {
        return Collections.unmodifiableMap(outputConfigMap);
    }
}
