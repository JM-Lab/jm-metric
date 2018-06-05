package kr.jm.metric.builder;

import kr.jm.metric.config.mutating.MutatingConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Raw field map builder.
 */
public class RawFieldMapBuilder extends
        AbstractFieldMapBuilder<MutatingConfig> {

    private Map<String, Object> rawMetricConfigMap = Map.of("rawData", true);

    @Override
    public Map<String, Object> buildFieldObjectMap(
            MutatingConfig inputConfig, String targetString) {
        return new HashMap<>(rawMetricConfigMap);
    }

}
