package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.DelimiterMutatorConfig;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMStream;
import lombok.ToString;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class DelimiterMutator extends
        AbstractMutator<DelimiterMutatorConfig> {

    public static final String DefaultIndexHeader = "Index-";
    protected String[] fields;
    private Pattern splitPattern;
    private Pattern discardPattern;

    public DelimiterMutator() {
        this(new DelimiterMutatorConfig("Delimiter"));
    }

    public DelimiterMutator(DelimiterMutatorConfig mutatorConfig) {
        super(mutatorConfig);
        this.splitPattern = Pattern.compile(JMOptional.getOptional(mutatorConfig
                .getDelimiterRegex()).orElse("\\s+"));
        JMOptional.getOptional(mutatorConfig.getFields())
                .ifPresent(fields -> this.fields = fields);
        Optional.ofNullable(mutatorConfig.getDiscardRegex())
                .map(Pattern::compile)
                .ifPresent(pattern -> this.discardPattern = pattern);
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        try {
            return buildFieldObjectMap(
                    applyDiscardRegex(splitPattern.split(targetString)));
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturn(log, e, "buildFieldObjectMap",
                            Collections::emptyMap, Arrays.toString(fields),
                            splitPattern, discardPattern, targetString);
        }
    }

    protected Map<String, Object> buildFieldObjectMap(String[] splitValues) {
        return buildFieldObjectMap(getFields(splitValues), splitValues);
    }

    private Map<String, Object> buildFieldObjectMap(String[] fields,
            String[] values) {
        if (fields.length > values.length)
            throw new RuntimeException("Wrong Fields Or Values !!!");
        return JMStream.increaseRange(fields.length).boxed()
                .collect(Collectors.toMap(i -> fields[i], i -> values[i]));
    }

    private String[] getFields(String[] splitValues) {
        return JMOptional.getOptional(this.fields).orElseGet(
                () -> this.fields = JMStream.increaseRange(splitValues.length)
                        .mapToObj(i -> DefaultIndexHeader + i)
                        .toArray(String[]::new));
    }

    private String[] applyDiscardRegex(String[] values) {
        return Objects.nonNull(this.discardPattern) ? Arrays.stream(values)
                .map(this::applyDiscardRegex).toArray(String[]::new) : values;
    }

    private String applyDiscardRegex(String keyValueString) {
        return this.discardPattern.matcher(keyValueString).replaceAll("");
    }

}
