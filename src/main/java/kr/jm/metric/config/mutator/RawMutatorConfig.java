package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfigBuilder;
import kr.jm.metric.mutator.RawMutator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class RawMutatorConfig extends AbstractMutatorConfig {

    public RawMutatorConfig(String mutatorId) {
        super(mutatorId, MutatorConfigType.RAW,
                new FieldConfigBuilder().setRawData(true).createFieldConfig());
    }


    @Override
    public RawMutator buildMutator() {
        return new RawMutator();
    }
}
