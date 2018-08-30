package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.KeyValueDelimiterMutatorConfig;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.exception.JMExceptionManager;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The type Key value delimiter field map mutator.
 */
@ToString(callSuper = true)
public class KeyValueDelimiterMutator extends DelimiterMutator {

    private Pattern keyValuePattern;

    public KeyValueDelimiterMutator(
            KeyValueDelimiterMutatorConfig mutatorConfig) {
        super(mutatorConfig);
        this.keyValuePattern =
                Pattern.compile(mutatorConfig.getKeyValueDelimiterRegex());
    }

    /**
     * Instantiates a new Key value delimiter field map mutator.
     */
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
            return JMExceptionManager.handleExceptionAndThrowRuntimeEx(log, e,
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
