package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.KeyValueDelimiterMutatorConfig;
import kr.jm.utils.JMArrays;
import kr.jm.utils.exception.JMException;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class KeyValueDelimiterMutator extends DelimiterMutator {

    private final Pattern keyValuePattern;

    public KeyValueDelimiterMutator(
            KeyValueDelimiterMutatorConfig mutatorConfig) {
        super(mutatorConfig);
        this.keyValuePattern =
                Pattern.compile(mutatorConfig.getKeyValueDelimiterRegex());
    }

    public KeyValueDelimiterMutator() {
        this(new KeyValueDelimiterMutatorConfig("KeyValueDelimiterMutator"));
    }

    @Override
    protected Map<String, Object> buildFieldObjectMap(String[] splitValues) {
        try {
            return buildFieldObjectMap(
                    Arrays.stream(splitValues).map(this.keyValuePattern::split)
                            .collect(Collectors.toMap(array -> array[0],
                                    array ->
                                            array.length > 1 ? array[1] : "")));
        } catch (Exception e) {
            return JMException.handleExceptionAndThrowRuntimeEx(log, e,
                    "buildFieldObjectMap", this.keyValuePattern, splitValues);
        }

    }

    private Map<String, Object> buildFieldObjectMap(
            Map<String, String> fieldValueMap) {
        this.fields = JMArrays.toArray(fieldValueMap.keySet());
        return super
                .buildFieldObjectMap(JMArrays.toArray(fieldValueMap.values()));
    }
}
