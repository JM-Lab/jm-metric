package kr.jm.metric.config;

import kr.jm.utils.helper.JMJson;

import java.util.Map;

public interface ConfigInterface {
    default Map<String, Object> extractConfigMap() {
        return JMJson.transformToMap(this);
    }
}
