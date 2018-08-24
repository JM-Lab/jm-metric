package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.JsonMutator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Json mutator config.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class JsonMutatorConfig extends AbstractMutatorConfig {

    /**
     * Instantiates a new Json mutator config.
     *
     * @param mutatorId the mutator id
     */
    public JsonMutatorConfig(String mutatorId) {
        this(mutatorId, null);
    }

    /**
     * Instantiates a new Json mutator config.
     *
     * @param mutatorId   the mutator id
     * @param fieldConfig the field config
     */
    public JsonMutatorConfig(String mutatorId, FieldConfig fieldConfig) {
        this(mutatorId, fieldConfig, false);
    }

    /**
     * Instantiates a new Json mutator config.
     *
     * @param mutatorId   the mutator id
     * @param fieldConfig the field config
     * @param isJsonList  the is json list
     */
    public JsonMutatorConfig(String mutatorId, FieldConfig fieldConfig,
            boolean isJsonList) {
        super(mutatorId, MutatorConfigType.JSON, fieldConfig);
        if (isJsonList)
            this.chunkType = ChunkType.JSON_LIST;
    }


    @Override
    public JsonMutator buildMutator() {
        return new JsonMutator(this);
    }
}
