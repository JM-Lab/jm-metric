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
        super(mutatorId, MutatorConfigType.JSON, fieldConfig);
    }


    @Override
    public JsonMutator buildMutator() {
        return new JsonMutator(this);
    }
}
