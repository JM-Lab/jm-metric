package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.FormattedMutatorConfig;
import kr.jm.utils.JMRegex;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FormattedMutator extends
        AbstractMutator<FormattedMutatorConfig> {
    @Getter
    private String valueRegex;
    JMRegex jmRegex;

    public FormattedMutator(
            FormattedMutatorConfig formattedMutatorConfig) {
        this(formattedMutatorConfig, formattedMutatorConfig.getFieldNameMap());
    }

    public FormattedMutator(
            FormattedMutatorConfig formattedMutatorConfig,
            Map<String, String> defaultFieldNameMap) {
        super(formattedMutatorConfig);
        this.valueRegex =
                formattedMutatorConfig.isWordValueRegex() ? "\\S+" : ".+";
        Map<String, String> fieldNameMap = initFieldNameMap(new HashMap<>(
                        JMOptional.getOptional(defaultFieldNameMap)
                                .orElseGet(Collections::emptyMap)),
                formattedMutatorConfig.getFieldNameMap());
        Map<String, String> fieldGroupRegexMap =
                JMMap.newChangedKeyValueWithEntryMap(fieldNameMap,
                        Map.Entry::getKey,
                        entry -> buildPartGroupRegex(entry.getKey(),
                                entry.getValue()));
        this.jmRegex = new JMRegex(initGroupRegexString(fieldGroupRegexMap,
                formattedMutatorConfig.getFormat()));
    }

    private Map<String, String> initFieldNameMap(
            Map<String, String> defaultFieldNameMap,
            Map<String, String> fieldNameMap) {
        JMOptional.getOptional(fieldNameMap)
                .ifPresent(defaultFieldNameMap::putAll);
        return defaultFieldNameMap;
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

    protected String initGroupRegexString(
            Map<String, String> fieldGroupRegexMap, String formatString) {
        for (String field : fieldGroupRegexMap.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()))
            formatString = formatString
                    .replace(field, fieldGroupRegexMap.get(field));
        return formatString;
    }

    public List<String> getFieldList() {
        return jmRegex.getGroupNameList();
    }

    protected String buildPartGroupRegex(String field, String name) {
        return "(?<" + name + ">" + this.valueRegex + ")";
    }

}
