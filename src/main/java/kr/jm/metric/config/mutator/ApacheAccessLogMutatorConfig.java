package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.ApacheAccessLogMutator;
import lombok.ToString;

import java.util.Map;

@ToString(callSuper = true)
public class ApacheAccessLogMutatorConfig extends FormattedMutatorConfig {

    protected ApacheAccessLogMutatorConfig() {
    }

    public ApacheAccessLogMutatorConfig(String mutatorId,
            String apacheCommonLogFormat) {
        this(mutatorId, null, apacheCommonLogFormat);
    }

    public ApacheAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String apacheCommonLogFormat) {
        this(mutatorId, fieldConfig, apacheCommonLogFormat, null);
    }

    public ApacheAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String apacheCommonLogFormat,
            Map<String, String> fieldNameMap) {
        super(mutatorId, MutatorConfigType.APACHE_ACCESS_LOG, false,
                apacheCommonLogFormat, fieldNameMap, fieldConfig);
    }

    @Override
    public ApacheAccessLogMutator buildMutator() {
        return new ApacheAccessLogMutator(this);
    }
}
