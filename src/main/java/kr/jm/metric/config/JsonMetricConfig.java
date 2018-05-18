package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Json metric properties.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class JsonMetricConfig extends MetricConfig {

    /**
     * Instantiates a new Json metric properties.
     *
     * @param configId the properties id
     */
    public JsonMetricConfig(String configId) {
        this(configId, null);
    }

    /**
     * Instantiates a new Json metric properties.
     *
     * @param configId    the properties id
     * @param fieldConfig the field properties
     */
    public JsonMetricConfig(String configId, FieldConfig fieldConfig) {
        this(configId, fieldConfig, false);
    }

    /**
     * Instantiates a new Json metric properties.
     *
     * @param configId    the properties id
     * @param fieldConfig the field properties
     * @param isJsonList  the is json list
     */
    public JsonMetricConfig(String configId, FieldConfig fieldConfig,
            boolean isJsonList) {
        super(configId, MetricConfigType.JSON, fieldConfig);
        if (isJsonList)
            this.chunkType = ChunkType.JSON_LIST;
    }


}
