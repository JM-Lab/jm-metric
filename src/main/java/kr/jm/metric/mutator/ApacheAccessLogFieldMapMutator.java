package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.ApacheAccessLogMutatorConfig;
import kr.jm.utils.helper.JMJson;
import lombok.ToString;

/**
 * The type Apache access log field map mutator.
 */
@ToString(callSuper = true)
public class ApacheAccessLogFieldMapMutator extends FormattedFieldMapMutator {

    /**
     * Instantiates a new Apache access log field map mutator.
     */
    public ApacheAccessLogFieldMapMutator() {
        this(new ApacheAccessLogMutatorConfig("ApacheAccessLogMutator",
                "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\""));
    }

    /**
     * Instantiates a new Apache access log field map mutator.
     *
     * @param apacheAccessLogMutatorConfig the apache access log mutator config
     */
    public ApacheAccessLogFieldMapMutator(
            ApacheAccessLogMutatorConfig apacheAccessLogMutatorConfig) {
        super(apacheAccessLogMutatorConfig, JMJson.withClasspathOrFilePath(
                "DefaultApacheAccessLogKeyNameMap.json",
                JMJson.getMapOrListTypeReference()));
    }

    @Override
    protected String buildPartGroupRegex(String field, String name) {
        String groupRegex = super.buildPartGroupRegex(field, name);
        return !field.equals("%t") ? groupRegex : "\\[" + groupRegex + "\\]";
    }
}
