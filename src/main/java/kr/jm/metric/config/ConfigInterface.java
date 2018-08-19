package kr.jm.metric.config;

import kr.jm.utils.helper.JMJson;

import java.util.Map;

/**
 * The interface Config interface.
 */
public interface ConfigInterface {

    /**
     * Extract config map map.
     *
     * @return the map
     */
    default Map<String, Object> extractConfigMap() {
        return JMJson.transformToMap(this);
    }

    /**
     * Transform config c.
     *
     * @param <C>         the type parameter
     * @param configClass the config class
     * @return the c
     */
    default <C extends ConfigInterface> C transformConfig(
            Class<C> configClass) {
        return transformConfig(extractConfigMap(), configClass);
    }

    /**
     * Transform config c.
     *
     * @param <C>         the type parameter
     * @param configMap   the config map
     * @param configClass the config class
     * @return the c
     */
    static <C extends ConfigInterface> C transformConfig(
            Map<String, Object> configMap, Class<C> configClass) {
        return JMJson.transform(configMap, configClass);
    }
}
