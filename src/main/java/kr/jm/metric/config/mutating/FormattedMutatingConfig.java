package kr.jm.metric.config.mutating;

import kr.jm.metric.config.mutating.field.FieldConfig;
import kr.jm.metric.mutating.builder.FormattedFieldMapBuilder;
import kr.jm.utils.datastructure.JMArrays;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * The type Formatted metric properties.
 */
@ToString(callSuper = true)
public class FormattedMutatingConfig extends MutatingConfig {
    private String format;
    private Map<String, String> fieldNameMap;

    /**
     * Instantiates a new Formatted metric properties.
     */
    protected FormattedMutatingConfig() {
    }

    /**
     * Instantiates a new Formatted metric properties.
     *
     * @param configId the properties id
     * @param format   the format
     */
    public FormattedMutatingConfig(String configId, String format) {
        this(configId, format, Collections.emptyMap());
    }

    /**
     * Instantiates a new Formatted metric properties.
     *
     * @param configId    the properties id
     * @param format      the format
     * @param fieldConfig the field properties
     */
    public FormattedMutatingConfig(String configId, String format,
            FieldConfig fieldConfig) {
        this(configId, format, Collections.emptyMap(), fieldConfig);
    }

    /**
     * Instantiates a new Formatted metric properties.
     *
     * @param configId     the properties id
     * @param format       the format
     * @param fieldNameMap the field name map
     */
    public FormattedMutatingConfig(String configId, String format,
            Map<String, String> fieldNameMap) {
        this(configId, format, fieldNameMap, null);
    }

    /**
     * Instantiates a new Formatted metric properties.
     *
     * @param configId     the properties id
     * @param format       the format
     * @param fieldNameMap the field name map
     * @param fieldConfig  the field properties
     */
    public FormattedMutatingConfig(String configId, String format,
            Map<String, String> fieldNameMap, FieldConfig fieldConfig) {
        this(configId, MutatingConfigType.FORMATTED, format, fieldNameMap,
                fieldConfig);
    }

    /**
     * Instantiates a new Formatted metric properties.
     *
     * @param configId         the properties id
     * @param mutatingConfigType the metric properties type
     * @param format           the format
     * @param fieldConfig      the field properties
     */
    public FormattedMutatingConfig(String configId,
            MutatingConfigType mutatingConfigType, String format,
            FieldConfig fieldConfig) {
        this(configId, mutatingConfigType, format, Collections.emptyMap(),
                fieldConfig);
    }

    /**
     * Instantiates a new Formatted metric properties.
     *
     * @param configId         the properties id
     * @param mutatingConfigType the metric properties type
     * @param format           the format
     * @param fieldNameMap     the field name map
     * @param fieldConfig      the field properties
     */
    public FormattedMutatingConfig(String configId,
            MutatingConfigType mutatingConfigType, String format,
            Map<String, String> fieldNameMap, FieldConfig fieldConfig) {
        super(configId, mutatingConfigType, fieldConfig);
        this.format = format;
        this.fieldNameMap = fieldNameMap;
    }

    @Override
    public String[] getFields() {
        return JMArrays.toArray(((FormattedFieldMapBuilder) getMetricBuilder())
                .getFieldList(getConfigId()));
    }

    /**
     * Gets format.
     *
     * @return the format
     */
    public String getFormat() {return this.format;}

    /**
     * Gets field name map.
     *
     * @return the field name map
     */
    public Map<String, String> getFieldNameMap() {
        return fieldNameMap;
    }

}
