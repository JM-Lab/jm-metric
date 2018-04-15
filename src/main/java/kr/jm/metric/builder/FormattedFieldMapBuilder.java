package kr.jm.metric.builder;

import kr.jm.metric.config.FormattedMetricConfig;
import kr.jm.utils.JMRegex;
import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMOptional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Formatted field map builder.
 */
public class FormattedFieldMapBuilder extends
        AbstractFieldMapBuilder<FormattedMetricConfig> {
    private static Map<String, JMRegex> jmRegexCache = new HashMap<>();
    private static Map<String, String> namePartGroupRegexCache =
            new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static JMNestedMap<String, String, String> fieldGroupRegexMapCache =
            new JMNestedMap<>();
    private String valueRegex;
    private Map<String, String> defaultFieldNameMap;

    /**
     * Instantiates a new Formatted field map builder.
     */
    public FormattedFieldMapBuilder() {
        this(true, null);
    }

    /**
     * Gets value regex.
     *
     * @return the value regex
     */
    public String getValueRegex() {
        return valueRegex;
    }

    /**
     * Gets default field name map.
     *
     * @return the default field name map
     */
    public Map<String, String> getDefaultFieldNameMap() {
        return Collections.unmodifiableMap(defaultFieldNameMap);
    }

    /**
     * Instantiates a new Formatted field map builder.
     *
     * @param isWordValueRegex    the is word value regex
     * @param defaultFieldNameMap the default field name map
     */
    public FormattedFieldMapBuilder(boolean isWordValueRegex,
            Map<String, String> defaultFieldNameMap) {
        this.valueRegex = isWordValueRegex ? "\\S+" : ".+";
        this.defaultFieldNameMap = JMOptional.getOptional
                (defaultFieldNameMap).orElseGet(Collections::emptyMap);
    }

    /**
     * Gets field list.
     *
     * @param configId the config id
     * @return the field list
     */
    public List<String> getFieldList(String configId) {
        return JMOptional.getOptional(jmRegexCache, configId)
                .map(JMRegex::getGroupNameList)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(
            FormattedMetricConfig inputConfig, String targetString) {
        return new HashMap<>(getJMRegex(
                buildGroupRegexString(inputConfig.getConfigId(),
                        inputConfig.getFormat(), inputConfig.getFieldNameMap()))
                .getGroupNameValueMap(targetString));
    }

    private JMRegex getJMRegex(String groupRegexString) {
        return JMMap.getOrPutGetNew(jmRegexCache, groupRegexString,
                () -> new JMRegex(groupRegexString));
    }

    /**
     * Build group regex string string.
     *
     * @param fieldGroupRegexMap the field group regex map
     * @param formatString       the format string
     * @return the string
     */
    protected String buildGroupRegexString(
            Map<String, String> fieldGroupRegexMap, String formatString) {
        for (String field : fieldGroupRegexMap.keySet())
            if (fieldGroupRegexMap.containsKey(field))
                formatString = formatString
                        .replace(field, fieldGroupRegexMap.get(field));
        return formatString;
    }

    /**
     * Build group regex string string.
     *
     * @param configId     the config id
     * @param formatString the format string
     * @param fieldNameMap the field name map
     * @return the string
     */
    public String buildGroupRegexString(String configId, String formatString,
            Map<String, String> fieldNameMap) {
        return buildGroupRegexString(
                getFieldGroupRegexMap(configId, fieldNameMap), formatString);
    }

    private Map<String, String> getFieldGroupRegexMap(String configId,
            Map<String, String> fieldNameMap) {
        return fieldGroupRegexMapCache.getOrPutGetNew(configId,
                () -> buildGroupRegexMap(fieldNameMap));
    }

    private Map<String, String> buildGroupRegexMap(
            Map<String, String> fieldNameMap) {
        return JMMap.newChangedKeyValueWithEntryMap(
                buildFinalFieldNameMap(fieldNameMap), Map.Entry::getKey,
                entry -> getPartGroupRegex(entry.getKey(), entry.getValue()));
    }

    private Map<String, String> buildFinalFieldNameMap(
            Map<String, String> fieldNameMap) {
        Map<String, String> newFieldNameMap =
                new HashMap<>(defaultFieldNameMap);
        JMOptional.getOptional(fieldNameMap).ifPresent(newFieldNameMap::putAll);
        return newFieldNameMap;
    }


    private String getPartGroupRegex(String field, String name) {
        return JMMap.getOrPutGetNew(namePartGroupRegexCache, field + name,
                () -> buildPartGroupRegex(field, name));
    }

    /**
     * Build part group regex string.
     *
     * @param field the field
     * @param name  the name
     * @return the string
     */
    protected String buildPartGroupRegex(String field, String name) {
        return "(?<" + name + ">" + valueRegex + ")";
    }
}
