package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import kr.jm.utils.helper.JMOptional;
import lombok.ToString;

/**
 * The type Key value delimiter metric properties.
 */
@ToString(callSuper = true)
public class KeyValueDelimiterMetricConfig extends DelimiterMetricConfig {
    /**
     * The constant DefaultKeyValueDelimiterRegex.
     */
    public static final String DefaultKeyValueDelimiterRegex = "=";
    private String keyValueDelimiterRegex;

    /**
     * Instantiates a new Key value delimiter metric properties.
     */
    protected KeyValueDelimiterMetricConfig() {
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId the properties id
     */
    public KeyValueDelimiterMetricConfig(String configId) {
        this(configId, DefaultKeyValueDelimiterRegex);
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId    the properties id
     * @param fieldConfig the field properties
     */
    public KeyValueDelimiterMetricConfig(String configId,
            FieldConfig fieldConfig) {
        this(configId, fieldConfig, DefaultKeyValueDelimiterRegex);
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId               the properties id
     * @param keyValueDelimiterRegex the key value delimiter regex
     */
    public KeyValueDelimiterMetricConfig(String configId,
            String keyValueDelimiterRegex) {
        this(configId, null, keyValueDelimiterRegex, null);
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId               the properties id
     * @param fieldConfig            the field properties
     * @param keyValueDelimiterRegex the key value delimiter regex
     */
    public KeyValueDelimiterMetricConfig(String configId,
            FieldConfig fieldConfig, String keyValueDelimiterRegex) {
        this(configId, fieldConfig, keyValueDelimiterRegex, null);
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId               the properties id
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param delimiter              the delimiter
     */
    public KeyValueDelimiterMetricConfig(String configId,
            String keyValueDelimiterRegex, String delimiter) {
        this(configId, null, keyValueDelimiterRegex, delimiter);
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId               the properties id
     * @param fieldConfig            the field properties
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param delimiter              the delimiter
     */
    public KeyValueDelimiterMetricConfig(String configId,
            FieldConfig fieldConfig, String keyValueDelimiterRegex,
            String delimiter) {
        this(configId, fieldConfig, keyValueDelimiterRegex, delimiter, null);
    }

    /**
     * Instantiates a new Key value delimiter metric properties.
     *
     * @param configId               the properties id
     * @param fieldConfig            the field properties
     * @param keyValueDelimiterRegex the key value delimiter regex
     * @param delimiter              the delimiter
     * @param discardRegex           the discard regex
     */
    public KeyValueDelimiterMetricConfig(String configId,
            FieldConfig fieldConfig, String keyValueDelimiterRegex,
            String delimiter, String discardRegex) {
        super(configId, MetricConfigType.KEY_VALUE_DELIMITER, delimiter,
                discardRegex, fieldConfig);
        this.keyValueDelimiterRegex =
                JMOptional.getOptional(keyValueDelimiterRegex)
                        .orElse(DefaultKeyValueDelimiterRegex);
    }

    /**
     * Gets key value delimiter regex.
     *
     * @return the key value delimiter regex
     */
    public String getKeyValueDelimiterRegex() {return this.keyValueDelimiterRegex;}

}
