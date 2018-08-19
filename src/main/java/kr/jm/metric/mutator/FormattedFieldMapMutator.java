package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.FormattedMutatorConfig;
import kr.jm.utils.JMRegex;
import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMOptional;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Formatted field map mutator.
 */
@ToString(callSuper = true)
public class FormattedFieldMapMutator extends
        AbstractFieldMapMutator<FormattedMutatorConfig> {
    private static Map<String, JMRegex> jmRegexCache = new HashMap<>();
    private static Map<String, String> namePartGroupRegexCache =
            new HashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static JMNestedMap<String, String, String> fieldGroupRegexMapCache =
            new JMNestedMap<>();
    private String valueRegex;
    private Map<String, String> fieldNameMap;

    /**
     * Instantiates a new Formatted field map mutator.
     *
     * @param formattedMutatorConfig the formatted mutator config
     */
    public FormattedFieldMapMutator(
            FormattedMutatorConfig formattedMutatorConfig) {
        this(formattedMutatorConfig,
                JMOptional.getOptional(formattedMutatorConfig.getFieldNameMap())
                        .orElseGet(Collections::emptyMap));
    }

    /**
     * Instantiates a new Formatted field map mutator.
     *
     * @param formattedMutatorConfig the formatted mutator config
     * @param defaultFieldNameMap    the default field name map
     */
    public FormattedFieldMapMutator(
            FormattedMutatorConfig formattedMutatorConfig,
            Map<String, String> defaultFieldNameMap) {
        super(formattedMutatorConfig);
        this.valueRegex =
                formattedMutatorConfig.isWordValueRegex() ? "\\S+" : ".+";
        this.fieldNameMap = initFieldNameMap(new HashMap<>(
                        JMOptional.getOptional(defaultFieldNameMap)
                                .orElseGet(Collections::emptyMap)),
                formattedMutatorConfig.getFieldNameMap());
    }

    private Map<String, String> initFieldNameMap(
            Map<String, String> defaultFieldNameMap,
            Map<String, String> fieldNameMap) {
        JMOptional.getOptional(fieldNameMap)
                .ifPresent(defaultFieldNameMap::putAll);
        return defaultFieldNameMap;
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
     * Gets field name map.
     *
     * @return the field name map
     */
    public Map<String, String> getFieldNameMap() {
        return Collections.unmodifiableMap(fieldNameMap);
    }

    /**
     * Gets field list.
     *
     * @param mutatorId the mutator id
     * @return the field list
     */
    public List<String> getFieldList(String mutatorId) {
        return JMOptional.getOptional(jmRegexCache, mutatorId)
                .map(JMRegex::getGroupNameList)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        return new HashMap<>(
                getJMRegex(buildGroupRegexString(getMutatorId(),
                        mutatorConfig.getFormat(),
                        mutatorConfig.getFieldNameMap()))
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
     * @param mutatorId    the mutator id
     * @param formatString the format string
     * @param fieldNameMap the field name map
     * @return the string
     */
    public String buildGroupRegexString(String mutatorId, String formatString,
            Map<String, String> fieldNameMap) {
        return buildGroupRegexString(
                getFieldGroupRegexMap(mutatorId, fieldNameMap), formatString);
    }

    private Map<String, String> getFieldGroupRegexMap(String mutatorId,
            Map<String, String> fieldNameMap) {
        return fieldGroupRegexMapCache.getOrPutGetNew(mutatorId,
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
                new HashMap<>(this.fieldNameMap);
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
