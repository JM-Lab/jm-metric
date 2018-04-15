package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import kr.jm.metric.builder.FieldMapBuilderInterface;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMOptional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Metric config.
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MetricConfig {
    /**
     * The Config id.
     */
    @Getter
    protected String configId;
    /**
     * The Metric config type.
     */
    @Getter
    protected MetricConfigType metricConfigType;
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
     * The Bind data ids.
     */
    protected Set<String> bindDataIds;

    /**
     * The Chunk type.
     */
    @Getter
    protected ChunkType chunkType;

    /**
     * Instantiates a new Metric config.
     *
     * @param configId         the config id
     * @param metricConfigType the metric config type
     */
    public MetricConfig(String configId, MetricConfigType metricConfigType) {
        this(configId, metricConfigType, null);
    }

    /**
     * Instantiates a new Metric config.
     *
     * @param configId         the config id
     * @param metricConfigType the metric config type
     * @param fieldConfig      the field config
     * @param fields           the fields
     */
    public MetricConfig(String configId, MetricConfigType metricConfigType,
            FieldConfig fieldConfig, String... fields) {
        this.configId = configId;
        this.metricConfigType = metricConfigType;
        this.fieldConfig = fieldConfig;
        this.fields = fields;
    }

    /**
     * Gets metric builder.
     *
     * @return the metric builder
     */
    public FieldMapBuilderInterface getMetricBuilder() {
        return this.metricConfigType.getFieldMapBuilder();
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

    /**
     * Gets bind data ids.
     *
     * @return the bind data ids
     */
    public Set<String> getBindDataIds() {
        return JMLambda.supplierIfNull(this.bindDataIds,
                () -> this.bindDataIds = Collections.synchronizedSet(new
                        HashSet<>()));
    }

    /**
     * With bind data ids metric config.
     *
     * @param dataIds the data ids
     * @return the metric config
     */
    public MetricConfig withBindDataIds(String... dataIds) {
        JMOptional.getOptional(dataIds).map(Arrays::asList)
                .ifPresent(getBindDataIds()::addAll);
        return this;
    }

    /**
     * Remove bind data id boolean.
     *
     * @param dataId the data id
     * @return the boolean
     */
    public boolean removeBindDataId(String dataId) {
        return getBindDataIds().remove(dataId);
    }

}
