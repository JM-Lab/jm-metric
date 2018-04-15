package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import kr.jm.metric.builder.FormattedFieldMapBuilder;
import kr.jm.utils.datastructure.JMArrays;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * The type Formatted metric config.
 */
@ToString(callSuper = true)
public class FormattedMetricConfig extends MetricConfig {
    private String format;
    private Map<String, String> fieldNameMap;

    /**
     * Instantiates a new Formatted metric config.
     */
    protected FormattedMetricConfig() {
    }

    /**
     * Instantiates a new Formatted metric config.
     *
     * @param configId the config id
     * @param format   the format
     */
    public FormattedMetricConfig(String configId, String format) {
        this(configId, format, Collections.emptyMap());
    }

    /**
     * Instantiates a new Formatted metric config.
     *
     * @param configId    the config id
     * @param format      the format
     * @param fieldConfig the field config
     */
    public FormattedMetricConfig(String configId, String format,
            FieldConfig fieldConfig) {
        this(configId, format, Collections.emptyMap(), fieldConfig);
    }

    /**
     * Instantiates a new Formatted metric config.
     *
     * @param configId     the config id
     * @param format       the format
     * @param fieldNameMap the field name map
     */
    public FormattedMetricConfig(String configId, String format,
            Map<String, String> fieldNameMap) {
        this(configId, format, fieldNameMap, null);
    }

    /**
     * Instantiates a new Formatted metric config.
     *
     * @param configId     the config id
     * @param format       the format
     * @param fieldNameMap the field name map
     * @param fieldConfig  the field config
     */
    public FormattedMetricConfig(String configId, String format,
            Map<String, String> fieldNameMap, FieldConfig fieldConfig) {
        this(configId, MetricConfigType.FORMATTED, format, fieldNameMap,
                fieldConfig);
    }

    /**
     * Instantiates a new Formatted metric config.
     *
     * @param configId         the config id
     * @param metricConfigType the metric config type
     * @param format           the format
     * @param fieldConfig      the field config
     */
    public FormattedMetricConfig(String configId,
            MetricConfigType metricConfigType, String format,
            FieldConfig fieldConfig) {
        this(configId, metricConfigType, format, Collections.emptyMap(),
                fieldConfig);
    }

    /**
     * Instantiates a new Formatted metric config.
     *
     * @param configId         the config id
     * @param metricConfigType the metric config type
     * @param format           the format
     * @param fieldNameMap     the field name map
     * @param fieldConfig      the field config
     */
    public FormattedMetricConfig(String configId,
            MetricConfigType metricConfigType, String format,
            Map<String, String> fieldNameMap, FieldConfig fieldConfig) {
        super(configId, metricConfigType, fieldConfig);
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
