package kr.jm.metric.mutating.builder;

import kr.jm.metric.config.mutating.JsonMutatingConfig;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMJson;

import java.util.Map;

/**
 * The type Json field map builder.
 */
public class JsonFieldMapBuilder extends
        AbstractFieldMapBuilder<JsonMutatingConfig> {
    @Override
    public Map<String, Object> buildFieldObjectMap(
            JsonMutatingConfig inputConfig,
            String targetString) {
        return JMMap.newFlatKeyMap(JMJson.toMap(targetString));
    }

}
