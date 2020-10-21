package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.FormattedMutatorConfig;
import kr.jm.metric.config.mutator.NginxAccessLogMutatorConfig;
import kr.jm.utils.helper.JMJson;
import lombok.ToString;

import java.util.Map;

@ToString(callSuper = true)
public class NginxAccessLogMutator extends FormattedMutator {

    public NginxAccessLogMutator() {
        this(new NginxAccessLogMutatorConfig("NginxLogMutator",
                "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\""));
    }

    public NginxAccessLogMutator(
            FormattedMutatorConfig formattedMutatorConfig) {
        super(formattedMutatorConfig,
                JMJson.getInstance().withClasspathOrFilePath("DefaultNginxAccessLogKeyNameMap.json",
                        JMJson.getInstance().getMapOrListTypeReference()));
    }

    @Override
    protected String initGroupRegexString(
            Map<String, String> fieldGroupRegexMap,
            String formatString) {
        return super.initGroupRegexString(fieldGroupRegexMap, formatString)
                .replaceAll("[\\[\\]]", "\\\\$0");
    }
}
