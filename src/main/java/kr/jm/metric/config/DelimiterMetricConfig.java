package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import lombok.ToString;

/**
 * The type Delimiter metric config.
 */
@ToString(callSuper = true)
public class DelimiterMetricConfig extends MetricConfig {
    private String delimiterRegex;
    private String discardRegex;

    /**
     * Instantiates a new Delimiter metric config.
     */
    protected DelimiterMetricConfig() {
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId the config id
     */
    public DelimiterMetricConfig(String configId) {
        this(configId, "");
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId       the config id
     * @param delimiterRegex the delimiter regex
     */
    public DelimiterMetricConfig(String configId,
            String delimiterRegex) {
        this(configId, delimiterRegex, "");
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId the config id
     * @param fields   the fields
     */
    public DelimiterMetricConfig(String configId, String[] fields) {
        this(configId, "", fields);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId    the config id
     * @param fieldConfig the field config
     * @param fields      the fields
     */
    public DelimiterMetricConfig(String configId, FieldConfig fieldConfig,
            String... fields) {
        this(configId, null, fieldConfig, fields);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId       the config id
     * @param delimiterRegex the delimiter regex
     * @param fields         the fields
     */
    public DelimiterMetricConfig(String configId, String delimiterRegex,
            String[] fields) {
        this(configId, delimiterRegex, "", fields);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId       the config id
     * @param delimiterRegex the delimiter regex
     * @param fieldConfig    the field config
     * @param fields         the fields
     */
    public DelimiterMetricConfig(String configId, String delimiterRegex,
            FieldConfig fieldConfig, String... fields) {
        this(configId, delimiterRegex, null, fieldConfig, fields);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId       the config id
     * @param delimiterRegex the delimiter regex
     * @param discardRegex   the discard regex
     */
    public DelimiterMetricConfig(String configId, String delimiterRegex,
            String discardRegex) {
        this(configId, delimiterRegex, discardRegex, null);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId       the config id
     * @param delimiterRegex the delimiter regex
     * @param discardRegex   the discard regex
     * @param fields         the fields
     */
    public DelimiterMetricConfig(String configId, String delimiterRegex,
            String discardRegex, String[] fields) {
        this(configId, delimiterRegex, discardRegex, null, fields);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId       the config id
     * @param delimiterRegex the delimiter regex
     * @param discardRegex   the discard regex
     * @param fieldConfig    the field config
     * @param fields         the fields
     */
    public DelimiterMetricConfig(String configId, String delimiterRegex,
            String discardRegex, FieldConfig fieldConfig,
            String... fields) {
        this(configId, MetricConfigType.DELIMITER, delimiterRegex,
                discardRegex, fieldConfig, fields);
    }

    /**
     * Instantiates a new Delimiter metric config.
     *
     * @param configId         the config id
     * @param metricConfigType the metric config type
     * @param delimiterRegex   the delimiter regex
     * @param discardRegex     the discard regex
     * @param fieldConfig      the field config
     * @param fields           the fields
     */
    protected DelimiterMetricConfig(String configId,
            MetricConfigType metricConfigType,
            String delimiterRegex, String discardRegex,
            FieldConfig fieldConfig, String... fields) {
        super(configId, metricConfigType, fieldConfig, fields);
        this.delimiterRegex = delimiterRegex;
        this.discardRegex = discardRegex;
    }

    /**
     * Sets fields.
     *
     * @param fields the fields
     */
    public void setFields(String... fields) {
        this.fields = fields;
    }

    /**
     * Gets delimiter regex.
     *
     * @return the delimiter regex
     */
    public String getDelimiterRegex() {return this.delimiterRegex;}

    /**
     * Gets discard regex.
     *
     * @return the discard regex
     */
    public String getDiscardRegex() {return this.discardRegex;}

}
