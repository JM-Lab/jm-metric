package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import lombok.ToString;

import java.util.Map;

/**
 * The type Nginx access log metric properties.
 */
@ToString(callSuper = true)
public class NginxAccessLogMetricConfig extends FormattedMetricConfig {

    /**
     * Instantiates a new Nginx access log metric properties.
     */
    protected NginxAccessLogMetricConfig() {
    }

    /**
     * Instantiates a new Nginx access log metric properties.
     *
     * @param configId       the properties id
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMetricConfig(String configId, String nginxLogFormat) {
        this(configId, null, nginxLogFormat);
    }

    /**
     * Instantiates a new Nginx access log metric properties.
     *
     * @param configId       the properties id
     * @param fieldConfig    the field properties
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMetricConfig(String configId, FieldConfig fieldConfig,
            String nginxLogFormat) {
        this(configId, fieldConfig, nginxLogFormat, null);
    }

    /**
     * Instantiates a new Nginx access log metric properties.
     *
     * @param configId       the properties id
     * @param fieldConfig    the field properties
     * @param nginxLogFormat the nginx log format
     * @param fieldNameMap   the field name map
     */
    public NginxAccessLogMetricConfig(String configId, FieldConfig fieldConfig,
            String nginxLogFormat, Map<String, String> fieldNameMap) {
        super(configId, MetricConfigType.NGINX_ACCESS_LOG, nginxLogFormat, fieldNameMap,
                fieldConfig);
    }

}
