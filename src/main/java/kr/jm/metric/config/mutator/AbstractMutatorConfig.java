package kr.jm.metric.config.mutator;

import kr.jm.metric.config.AbstractConfig;
import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMLambda;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Abstract mutator config.
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractMutatorConfig extends AbstractConfig implements
        MutatorConfigInterface {

    /**
     * The Mutator id.
     */
    @Getter
    protected String mutatorId;
    /**
     * The Mutator config type.
     */
    @Getter
    protected MutatorConfigType mutatorConfigType;
    /**
     * The Field config.
     */
    @Getter
    protected FieldConfig fieldConfig;
    /**
     * The Fields.
     */
    protected String[] fields;

    /**
     * The Chunk type.
     */
    @Getter
    protected ChunkType chunkType;

    /**
     * Instantiates a new Abstract mutator config.
     *
     * @param mutatorId         the mutator id
     * @param mutatorConfigType the mutator config type
     */
    public AbstractMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType) {
        this(mutatorId, mutatorConfigType, null);
    }

    /**
     * Instantiates a new Abstract mutator config.
     *
     * @param mutatorId         the mutator id
     * @param mutatorConfigType the mutator config type
     * @param fieldConfig       the field config
     * @param fields            the fields
     */
    public AbstractMutatorConfig(String mutatorId,
            MutatorConfigType mutatorConfigType,
            FieldConfig fieldConfig, String... fields) {
        this.mutatorId = mutatorId;
        this.mutatorConfigType = mutatorConfigType;
        this.fieldConfig = fieldConfig;
        this.fields = fields;
    }

    @Override
    public String[] getFields() {
        return JMLambda.supplierIfNull(this.fields,
                () -> this.fields = JMArrays.EMPTY_STRINGS);
    }

}
