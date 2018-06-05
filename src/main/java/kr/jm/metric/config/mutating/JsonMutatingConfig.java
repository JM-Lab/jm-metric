package kr.jm.metric.config.mutating;

import kr.jm.metric.config.mutating.field.FieldConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Json metric properties.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class JsonMutatingConfig extends MutatingConfig {

    /**
     * Instantiates a new Json metric properties.
     *
     * @param configId the properties id
     */
    public JsonMutatingConfig(String configId) {
        this(configId, null);
    }

    /**
     * Instantiates a new Json metric properties.
     *
     * @param configId    the properties id
     * @param fieldConfig the field properties
     */
    public JsonMutatingConfig(String configId, FieldConfig fieldConfig) {
        this(configId, fieldConfig, false);
    }

    /**
     * Instantiates a new Json metric properties.
     *
     * @param configId    the properties id
     * @param fieldConfig the field properties
     * @param isJsonList  the is json list
     */
    public JsonMutatingConfig(String configId, FieldConfig fieldConfig,
            boolean isJsonList) {
        super(configId, MutatingConfigType.JSON, fieldConfig);
        if (isJsonList)
            this.chunkType = ChunkType.JSON_LIST;
    }


}
