package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.JsonMutator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class JsonMutatorConfig extends AbstractMutatorConfig {

    public JsonMutatorConfig(String mutatorId) {
        this(mutatorId, null);
    }

    public JsonMutatorConfig(String mutatorId, FieldConfig fieldConfig) {
        super(mutatorId, MutatorConfigType.JSON, fieldConfig);
    }


    @Override
    public JsonMutator buildMutator() {
        return new JsonMutator(this);
    }
}
