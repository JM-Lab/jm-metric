package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.RawMutator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Raw mutator config.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class RawMutatorConfig extends AbstractMutatorConfig {

    /**
     * Instantiates a new Raw mutator config.
     *
     * @param mutatorId the mutator id
     */
    public RawMutatorConfig(String mutatorId) {
        super(mutatorId, MutatorConfigType.RAW, new FieldConfig(true));
    }


    @Override
    public RawMutator buildMutator() {
        return new RawMutator();
    }
}
