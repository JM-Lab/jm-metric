package kr.jm.metric.mutating.builder;

import kr.jm.utils.helper.JMJson;

import java.util.Map;

/**
 * The type Nginx access log field map builder.
 */
public class NginxAccessLogFieldMapBuilder extends FormattedFieldMapBuilder {

    /**
     * Instantiates a new Nginx access log field map builder.
     */
    public NginxAccessLogFieldMapBuilder() {
        this(JMJson.withClasspathOrFilePath
                ("defaultNginxAccessLogKeyNameMap.json",
                        JMJson.getMapOrListTypeReference()));
    }

    /**
     * Instantiates a new Nginx access log field map builder.
     *
     * @param defaultFieldNameMap the default field name map
     */
    public NginxAccessLogFieldMapBuilder(
            Map<String, String> defaultFieldNameMap) {
        super(false, defaultFieldNameMap);
    }

    @Override
    protected String buildGroupRegexString(
            Map<String, String> fieldGroupRegexMap,
            String formatString) {
        return super.buildGroupRegexString(fieldGroupRegexMap, formatString)
                .replaceAll("[\\[\\]]", "\\\\$0");
    }
}
