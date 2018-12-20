package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.ApacheAccessLogMutatorConfig;
import kr.jm.utils.helper.JMJson;
import lombok.ToString;

@ToString(callSuper = true)
public class ApacheAccessLogMutator extends FormattedMutator {

    public ApacheAccessLogMutator() {
        this(new ApacheAccessLogMutatorConfig("ApacheAccessLogMutator",
                "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\""));
    }

    public ApacheAccessLogMutator(
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
