package kr.jm.metric.config;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.helper.JMJson;

import java.util.Map;

public interface ConfigInterface {
    default Map<String, Object> extractConfigMap() {
        return JMJson.transformToMap(this);
    }

    static <C extends ConfigInterface> C transformConfig(C configInterface,
            TypeReference<C> typeReference) {
        return transformConfig(configInterface.extractConfigMap(),
                typeReference);
    }

    static <C extends ConfigInterface> C transformConfig(
            Map<String, Object> configMap, TypeReference<C> typeReference) {
        return JMJson.transform(configMap, typeReference);
    }

    static <C extends ConfigInterface> C transformConfig(C configInterface,
            Class<C> configClass) {
        return transformConfig(configInterface.extractConfigMap(), configClass);
    }

    static <C extends ConfigInterface> C transformConfig(
            Map<String, Object> configMap, Class<C> configClass) {
        return JMJson.transform(configMap, configClass);
    }
}
