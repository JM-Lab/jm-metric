package kr.jm.metric.output;

import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigType;
import kr.jm.utils.RestfulResourceUpdater;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OutputManager {

    private static final String OUTPUT_CONFIG_TYPE = "outputConfigType";
    private Set<String> outputConfigTypeSet;

    private Map<String, OutputConfigInterface> outputConfigMap;

    private Map<String, OutputInterface> outputMap;

    public OutputManager(String restfulResourceUrl) {
        this.outputConfigTypeSet = new HashSet<>(Arrays.stream(OutputConfigType
                .values()).map(Object::toString).collect(Collectors.toList()));
        new RestfulResourceUpdater<List<Map<String, Object>>>(
                restfulResourceUrl)
                .updateResource(list -> setOutputConfigMap(JMLambda.mapBy(list,
                        map -> map.get("configId").toString())));
        this.outputMap = new HashMap<>();
        JMLog.info(log, "OutputManager", restfulResourceUrl, JMJson
                .toPrettyJsonString(outputConfigMap.values()));
    }

    private void setOutputConfigMap(
            Map<String, Map<String, Object>> stringMapMap) {
        this.outputConfigMap = new HashMap<>();
        stringMapMap.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                        transform(entry.getValue())))
                .filter(entry -> Objects.nonNull(entry.getValue())).forEach
                (entry -> putOutputConfig(entry.getKey(), entry.getValue()));
    }

    private void putOutputConfig(String configId,
            OutputConfigInterface outputConfig) {
        this.outputConfigMap.put(configId, outputConfig);
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
