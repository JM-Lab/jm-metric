package kr.jm.metric.builder;

import kr.jm.metric.config.JsonMetricConfig;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMJson;

import java.util.Map;

/**
 * The type Json field map builder.
 */
public class JsonFieldMapBuilder extends
        AbstractFieldMapBuilder<JsonMetricConfig> {
    @Override
    public Map<String, Object> buildFieldObjectMap(JsonMetricConfig inputConfig,
            String targetString) {
        return JMMap.newFlatKeyMap(JMJson.toMap(targetString));
    }

}
