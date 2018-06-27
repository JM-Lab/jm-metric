package kr.jm.metric.mutating.builder;

import kr.jm.utils.helper.JMJson;

import java.util.Map;

/**
 * The type Apache access log field map builder.
 */
public class ApacheAccessLogFieldMapBuilder extends FormattedFieldMapBuilder {

    /**
     * Instantiates a new Apache access log field map builder.
     */
    public ApacheAccessLogFieldMapBuilder() {
        this(JMJson.withClasspathOrFilePath
                ("defaultApacheAccessLogKeyNameMap.json",
                        JMJson.getMapOrListTypeReference()));
    }

    /**
     * Instantiates a new Apache access log field map builder.
     *
     * @param defaultFieldNameMap the default field name map
     */
    public ApacheAccessLogFieldMapBuilder(Map<String, String> defaultFieldNameMap) {
        super(false, defaultFieldNameMap);
    }

    @Override
    protected String buildPartGroupRegex(String field, String name) {
        String groupRegex = super.buildPartGroupRegex(field, name);
        return !field.equals("%t") ? groupRegex : "\\[" + groupRegex + "\\]";
    }
}
