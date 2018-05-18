package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import lombok.ToString;

import java.util.Map;

/**
 * The type Apache access log metric properties.
 */
@ToString(callSuper = true)
public class ApacheAccessLogMetricConfig extends FormattedMetricConfig {

    /**
     * Instantiates a new Apache access log metric properties.
     */
    protected ApacheAccessLogMetricConfig() {
    }

    /**
     * Instantiates a new Apache access log metric properties.
     *
     * @param configId              the properties id
     * @param apacheCommonLogFormat the apache common log format
     */
    public ApacheAccessLogMetricConfig(String configId,
            String apacheCommonLogFormat) {
        this(configId, null, apacheCommonLogFormat);
    }

    /**
     * Instantiates a new Apache access log metric properties.
     *
     * @param configId              the properties id
     * @param fieldConfig           the field properties
     * @param apacheCommonLogFormat the apache common log format
     */
    public ApacheAccessLogMetricConfig(String configId, FieldConfig fieldConfig,
            String apacheCommonLogFormat) {
        this(configId, fieldConfig, apacheCommonLogFormat, null);
    }

    /**
     * Instantiates a new Apache access log metric properties.
     *
     * @param configId              the properties id
     * @param fieldConfig           the field properties
     * @param apacheCommonLogFormat the apache common log format
     * @param fieldNameMap          the field name map
     */
    public ApacheAccessLogMetricConfig(String configId, FieldConfig fieldConfig,
            String apacheCommonLogFormat, Map<String, String> fieldNameMap) {
        super(configId, MetricConfigType.APACHE_ACCESS_LOG, apacheCommonLogFormat,
                fieldNameMap, fieldConfig);
    }

}
