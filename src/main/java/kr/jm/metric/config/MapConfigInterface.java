package kr.jm.metric.config;

import kr.jm.utils.helper.JMJson;

import java.util.Map;

public interface MapConfigInterface extends ConfigInterface {

    default Map<String, Object> extractConfigMap() {
        return JMJson.transformToMap(this);
    }

    Map<String, Object> getProperties();

}
