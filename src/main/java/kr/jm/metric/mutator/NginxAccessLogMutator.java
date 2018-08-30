package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.FormattedMutatorConfig;
import kr.jm.metric.config.mutator.NginxAccessLogMutatorConfig;
import kr.jm.utils.helper.JMJson;
import lombok.ToString;

import java.util.Map;

/**
 * The type Nginx access log field map mutator.
 */
@ToString(callSuper = true)
public class NginxAccessLogMutator extends FormattedMutator {

    /**
     * Instantiates a new Nginx access log field map mutator.
     */
    public NginxAccessLogMutator() {
        this(new NginxAccessLogMutatorConfig("NginxLogMutator",
                "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\""));
    }

    /**
     * Instantiates a new Nginx access log field map mutator.
     *
     * @param formattedMutatorConfig the formatted mutator config
     */
    public NginxAccessLogMutator(
            FormattedMutatorConfig formattedMutatorConfig) {
        super(formattedMutatorConfig, JMJson.withClasspathOrFilePath
                ("DefaultNginxAccessLogKeyNameMap.json",
                        JMJson.getMapOrListTypeReference()));
    }

    @Override
    protected String buildGroupRegexString(
            Map<String, String> fieldGroupRegexMap,
            String formatString) {
        return super.buildGroupRegexString(fieldGroupRegexMap, formatString)
                .replaceAll("[\\[\\]]", "\\\\$0");
    }
}
