package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.NginxAccessLogMutator;
import lombok.ToString;

import java.util.Map;

/**
 * The type Nginx access log mutator config.
 */
@ToString(callSuper = true)
public class NginxAccessLogMutatorConfig extends FormattedMutatorConfig {

    /**
     * Instantiates a new Nginx access log mutator config.
     */
    protected NginxAccessLogMutatorConfig() {
    }

    /**
     * Instantiates a new Nginx access log mutator config.
     *
     * @param mutatorId      the mutator id
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMutatorConfig(String mutatorId,
            String nginxLogFormat) {
        this(mutatorId, null, nginxLogFormat);
    }

    /**
     * Instantiates a new Nginx access log mutator config.
     *
     * @param mutatorId      the mutator id
     * @param fieldConfig    the field config
     * @param nginxLogFormat the nginx log format
     */
    public NginxAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig,
            String nginxLogFormat) {
        this(mutatorId, fieldConfig, nginxLogFormat, null);
    }

    /**
     * Instantiates a new Nginx access log mutator config.
     *
     * @param mutatorId      the mutator id
     * @param fieldConfig    the field config
     * @param nginxLogFormat the nginx log format
     * @param fieldNameMap   the field name map
     */
    public NginxAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig,
            String nginxLogFormat, Map<String, String> fieldNameMap) {
        super(mutatorId, MutatorConfigType.NGINX_ACCESS_LOG, false,
                nginxLogFormat, fieldNameMap, fieldConfig);
    }

    @Override
    public NginxAccessLogMutator buildMutator() {
        return new NginxAccessLogMutator(this);
    }
}
