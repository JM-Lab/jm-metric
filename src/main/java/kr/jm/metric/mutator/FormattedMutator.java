package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.FormattedMutatorConfig;
import kr.jm.utils.JMRegex;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Formatted field map mutator.
 */
@ToString(callSuper = true)
public class FormattedMutator extends
        AbstractMutator<FormattedMutatorConfig> {
    private Map<String, String> namePartGroupRegexMap;
    private Map<String, String> fieldGroupRegexMap;
    private JMRegex jmRegex;
    private String valueRegex;
    private Map<String, String> fieldNameMap;

    /**
     * Instantiates a new Formatted field map mutator.
     *
     * @param formattedMutatorConfig the formatted mutator config
     */
    public FormattedMutator(
            FormattedMutatorConfig formattedMutatorConfig) {
        this(formattedMutatorConfig, formattedMutatorConfig.getFieldNameMap());
    }

    /**
     * Instantiates a new Formatted field map mutator.
     *
     * @param formattedMutatorConfig the formatted mutator config
     * @param defaultFieldNameMap    the default field name map
     */
    public FormattedMutator(
            FormattedMutatorConfig formattedMutatorConfig,
            Map<String, String> defaultFieldNameMap) {
        super(formattedMutatorConfig);
        this.valueRegex =
                formattedMutatorConfig.isWordValueRegex() ? "\\S+" : ".+";
        this.fieldNameMap = initFieldNameMap(new HashMap<>(
                        JMOptional.getOptional(defaultFieldNameMap)
                                .orElseGet(Collections::emptyMap)),
                formattedMutatorConfig.getFieldNameMap());
        this.namePartGroupRegexMap = new HashMap<>();
        this.fieldGroupRegexMap =
                JMMap.newChangedKeyValueWithEntryMap(this.fieldNameMap,
                        Map.Entry::getKey,
                        entry -> getPartGroupRegex(entry.getKey(),
                                entry.getValue()));
        this.jmRegex = new JMRegex(
                buildGroupRegexString(formattedMutatorConfig.getFormat()));
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
        return this.valueRegex;
    }

    /**
     * Gets field name map.
     *
     * @return the field name map
     */
    public Map<String, String> getFieldNameMap() {
        return Collections.unmodifiableMap(this.fieldNameMap);
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        return JMOptional
                .getOptional(this.jmRegex.getGroupNameValueMap(targetString))
                .map(map -> JMMap.newChangedValueMap(map, s -> (Object) s))
                .orElseGet(
                        () -> JMExceptionManager.handleExceptionAndReturn(log,
                                new RuntimeException("Parsing Error Occur !!!"),
                                "buildFieldObjectMap", Collections::emptyMap,
                                getMutatorId(), jmRegex, targetString));
    }

    protected String buildGroupRegexString(String formatString) {
        for (String field : this.fieldGroupRegexMap.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()))
            formatString =
                    formatString.replace(field, fieldGroupRegexMap.get(field));
        return formatString;
    }

    private String getPartGroupRegex(String field, String name) {
        return JMMap.getOrPutGetNew(this.namePartGroupRegexMap, field + name,
                () -> buildPartGroupRegex(field, name));
    }

    public List<String> getFieldList() {
        return jmRegex.getGroupNameList();
    }

    /**
     * Build part group regex string.
     *
     * @param field the field
     * @param name  the name
     * @return the string
     */
    protected String buildPartGroupRegex(String field, String name) {
        return "(?<" + name + ">" + this.valueRegex + ")";
    }
}
