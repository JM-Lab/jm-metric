package kr.jm.metric.config.mutator;

import kr.jm.metric.config.mutator.field.FieldConfig;
import kr.jm.metric.mutator.ApacheAccessLogFieldMapMutator;
import lombok.ToString;

import java.util.Map;

/**
 * The type Apache access log mutator config.
 */
@ToString(callSuper = true)
public class ApacheAccessLogMutatorConfig extends FormattedMutatorConfig {

    /**
     * Instantiates a new Apache access log mutator config.
     */
    protected ApacheAccessLogMutatorConfig() {
    }

    /**
     * Instantiates a new Apache access log mutator config.
     *
     * @param mutatorId             the mutator id
     * @param apacheCommonLogFormat the apache common log format
     */
    public ApacheAccessLogMutatorConfig(String mutatorId,
            String apacheCommonLogFormat) {
        this(mutatorId, null, apacheCommonLogFormat);
    }

    /**
     * Instantiates a new Apache access log mutator config.
     *
     * @param mutatorId             the mutator id
     * @param fieldConfig           the field config
     * @param apacheCommonLogFormat the apache common log format
     */
    public ApacheAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String apacheCommonLogFormat) {
        this(mutatorId, fieldConfig, apacheCommonLogFormat, null);
    }

    /**
     * Instantiates a new Apache access log mutator config.
     *
     * @param mutatorId             the mutator id
     * @param fieldConfig           the field config
     * @param apacheCommonLogFormat the apache common log format
     * @param fieldNameMap          the field name map
     */
    public ApacheAccessLogMutatorConfig(String mutatorId,
            FieldConfig fieldConfig, String apacheCommonLogFormat,
            Map<String, String> fieldNameMap) {
        super(mutatorId, MutatorConfigType.APACHE_ACCESS_LOG, false,
                apacheCommonLogFormat, fieldNameMap, fieldConfig);
    }

    @Override
    public ApacheAccessLogFieldMapMutator buildMutator() {
        return new ApacheAccessLogFieldMapMutator(this);
    }
}
