package kr.jm.metric.config.output;

import kr.jm.metric.config.AbstractConfigManager;
import kr.jm.metric.output.OutputInterface;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OutputConfigManager extends
        AbstractConfigManager<OutputConfigInterface> {
    private static final String OUTPUT_CONFIG_TYPE = "outputConfigType";
    private Map<String, OutputInterface> outputMap;

    public OutputConfigManager(String configFilename) {
        super(configFilename);
        this.outputMap = new HashMap<>();
    }

    @Override
    protected OutputConfigInterface transformToConfig(
            Map<String, Object> configMap) {
        return JMOptional.getOptional(configMap, OUTPUT_CONFIG_TYPE)
                .map(Object::toString)
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

    @Override
    protected String extractConfigId(OutputConfigInterface inputConfig) {
        return inputConfig.getOutputId();
    }

    public Optional<OutputInterface> getOutputAsOpt(String configId) {
        return JMOptional.getOptional(this.configMap, configId)
                .map(outputConfig -> JMMap
                        .getOrPutGetNew(this.outputMap, configId,
                                outputConfig::buildOutput));
    }

}
