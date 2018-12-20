package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.NginxAccessLogMutator;
import lombok.ToString;

import java.util.Map;

@ToString(callSuper = true)
public class NginxAccessLogMutatorConfig extends FormattedMutatorConfig {

    protected NginxAccessLogMutatorConfig() {
    }

    public NginxAccessLogMutatorConfig(String mutatorId,
            String nginxLogFormat) {
        this(mutatorId, null, nginxLogFormat);
    }

    public NginxAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig,
            String nginxLogFormat) {
        this(mutatorId, fieldConfig, nginxLogFormat, null);
    }

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
