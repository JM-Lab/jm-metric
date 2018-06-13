package kr.jm.metric.config.input;

import kr.jm.metric.config.AbstractConfigManager;
import kr.jm.metric.input.InputInterface;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;

import java.util.HashMap;
import java.util.Map;

public class InputConfigManager extends
        AbstractConfigManager<InputConfigInterface> {
    private static final String INPUT_CONFIG_TYPE = "inputConfigType";
    private Map<String, InputInterface> inputMap;

    public InputConfigManager(String configFilename) {
        super(configFilename);
        this.inputMap = new HashMap<>();
    }

    @Override
    protected InputConfigInterface transformToConfig(
            Map<String, Object> configMap) {
        return JMOptional.getOptional(configMap, INPUT_CONFIG_TYPE)
                .map(Object::toString)
                .map(InputConfigType::valueOf)
                .map(InputConfigType::getTypeReference)
                .map(typeReference -> JMJson
                        .transform(configMap, typeReference))
                .orElseGet(() -> JMExceptionManager
                        .handleExceptionAndReturnNull(log, JMExceptionManager
                                        .newRunTimeException(
                                                "No inputConfigType !!! - " + configMap
                                                        .get(INPUT_CONFIG_TYPE)),
                                "transform", configMap));
    }

    @Override
    protected String extractConfigId(InputConfigInterface inputConfig) {
        return inputConfig.getInputId();
    }


    public InputInterface getInput(String configId) {
        return JMOptional.getOptional(this.configMap, configId)
                .map(inputConfig -> JMMap
                        .getOrPutGetNew(this.inputMap, configId,
                                inputConfig::buildInput)).orElse(null);
    }

}
