package kr.jm.metric.config.mutating;

import kr.jm.metric.builder.FieldMapBuilderInterface;
import kr.jm.metric.config.AbstractConfig;
import kr.jm.metric.config.mutating.field.FieldConfig;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMLambda;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The type Metric properties.
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MutatingConfig extends AbstractConfig {

    @Getter
    protected String configId;
    /**
     * The Metric properties type.
     */
    @Getter
    protected MutatingConfigType mutatingConfigType;
    /**
     * The Field properties.
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
     * Instantiates a new Metric properties.
     *
     * @param configId           the properties id
     * @param mutatingConfigType the metric properties type
     */
    public MutatingConfig(String configId,
            MutatingConfigType mutatingConfigType) {
        this(configId, mutatingConfigType, null);
    }

    /**
     * Instantiates a new Metric properties.
     *
     * @param configId           the properties id
     * @param mutatingConfigType the metric properties type
     * @param fieldConfig        the field properties
     * @param fields             the fields
     */
    public MutatingConfig(String configId,
            MutatingConfigType mutatingConfigType,
            FieldConfig fieldConfig, String... fields) {
        this.configId = configId;
        this.mutatingConfigType = mutatingConfigType;
        this.fieldConfig = fieldConfig;
        this.fields = fields;
    }

    /**
     * Gets metric builder.
     *
     * @return the metric builder
     */
    public FieldMapBuilderInterface getMetricBuilder() {
        return this.mutatingConfigType.getFieldMapBuilder();
    }

    /**
     * Get fields string [ ].
     *
     * @return the string [ ]
     */
    public String[] getFields() {
        return JMLambda.supplierIfNull(this.fields,
                () -> this.fields = JMArrays.EMPTY_STRINGS);
    }

}
