package kr.jm.metric.config.mutator;

import kr.jm.metric.config.AbstractConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.utils.JMArrays;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMutatorConfig extends AbstractConfig implements
        MutatorConfigInterface {

    @Getter
    protected String mutatorId;
    @Getter
    protected MutatorConfigType mutatorConfigType;
    @Getter
    protected FieldConfig fieldConfig;
    protected String[] fields;

    @Getter
    protected int workers;

    public AbstractMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType) {
        this(mutatorId, mutatorConfigType, null);
    }

    public AbstractMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType,
            FieldConfig fieldConfig, String... fields) {
        this.mutatorId = mutatorId;
        this.mutatorConfigType = mutatorConfigType;
        this.fieldConfig = fieldConfig;
        this.fields = fields;
    }

    @Override
    public MutatorInterface buildMutator() {
        return null;
    }

    @Override
    public String[] getFields() {
        return Objects.requireNonNullElseGet(this.fields, () -> this.fields = JMArrays.EMPTY_STRINGS);
    }

}
