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
        super(apacheAccessLogMutatorConfig, JMJson.getInstance().withClasspathOrFilePath(
                "DefaultApacheAccessLogKeyNameMap.json",
                JMJson.getInstance().getMapOrListTypeReference()));
    }

    @Override
    protected String buildPartGroupRegex(String fieldKey, String fieldName) {
        String groupRegex = super.buildPartGroupRegex(fieldKey, fieldName);
        return !fieldKey.equals("%t") ? groupRegex : "\\[" + groupRegex + "\\]";
    }
}
