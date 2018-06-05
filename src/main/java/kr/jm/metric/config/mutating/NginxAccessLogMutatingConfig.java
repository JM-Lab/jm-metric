package kr.jm.metric.config.mutating;

import kr.jm.metric.config.mutating.field.FieldConfig;
import lombok.ToString;

import java.util.Map;

/**
 * The type Nginx access log metric properties.
 */
@ToString(callSuper = true)
public class NginxAccessLogMutatingConfig extends FormattedMutatingConfig {

    /**
     * Instantiates a new Nginx access log metric properties.
     */
    protected NginxAccessLogMutatingConfig() {
    }

    /**
     * Instantiates a new Nginx access log metric properties.
     *
     * @param configId       the properties id
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMutatingConfig(String configId,
            String nginxLogFormat) {
        this(configId, null, nginxLogFormat);
    }

    /**
     * Instantiates a new Nginx access log metric properties.
     *
     * @param configId       the properties id
     * @param fieldConfig    the field properties
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMutatingConfig(String configId,
            FieldConfig fieldConfig,
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
    public NginxAccessLogMutatingConfig(String configId,
            FieldConfig fieldConfig,
            String nginxLogFormat, Map<String, String> fieldNameMap) {
        super(configId, MutatingConfigType.NGINX_ACCESS_LOG, nginxLogFormat,
                fieldNameMap,
                fieldConfig);
    }

}
