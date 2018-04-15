package kr.jm.metric.config;

import kr.jm.metric.config.field.FieldConfig;
import lombok.ToString;

import java.util.Map;

/**
 * The type Nginx access log metric config.
 */
@ToString(callSuper = true)
public class NginxAccessLogMetricConfig extends FormattedMetricConfig {

    /**
     * Instantiates a new Nginx access log metric config.
     */
    protected NginxAccessLogMetricConfig() {
    }

    /**
     * Instantiates a new Nginx access log metric config.
     *
     * @param configId       the config id
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMetricConfig(String configId, String nginxLogFormat) {
        this(configId, null, nginxLogFormat);
    }

    /**
     * Instantiates a new Nginx access log metric config.
     *
     * @param configId       the config id
     * @param fieldConfig    the field config
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMetricConfig(String configId, FieldConfig fieldConfig,
            String nginxLogFormat) {
        this(configId, fieldConfig, nginxLogFormat, null);
    }

    /**
     * Instantiates a new Nginx access log metric config.
     *
     * @param configId       the config id
     * @param fieldConfig    the field config
     * @param nginxLogFormat the nginx log format
     * @param fieldNameMap   the field name map
     */
    public NginxAccessLogMetricConfig(String configId, FieldConfig fieldConfig,
            String nginxLogFormat, Map<String, String> fieldNameMap) {
        super(configId, MetricConfigType.NGINX_ACCESS_LOG, nginxLogFormat, fieldNameMap,
                fieldConfig);
    }

}
