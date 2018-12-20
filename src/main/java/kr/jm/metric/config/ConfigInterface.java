package kr.jm.metric.config;

import kr.jm.utils.helper.JMJson;

import java.util.Map;

public interface ConfigInterface {

    default Map<String, Object> extractConfigMap() {
        return JMJson.transformToMap(this);
    }

    default <C extends ConfigInterface> C transformConfig(
            Class<C> configClass) {
        return transformConfig(extractConfigMap(), configClass);
    }

    static <C extends ConfigInterface> C transformConfig(
            Map<String, Object> configMap, Class<C> configClass) {
        return JMJson.transform(configMap, configClass);
    }
}
