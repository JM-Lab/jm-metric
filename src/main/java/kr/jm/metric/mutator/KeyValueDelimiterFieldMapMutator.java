package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.KeyValueDelimiterMutatorConfig;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static kr.jm.metric.mutator.DelimiterFieldMapMutator.getSplitPattern;

/**
 * The type Key value delimiter field map mutator.
 */
@ToString(callSuper = true)
public class KeyValueDelimiterFieldMapMutator extends
        AbstractFieldMapMutator<KeyValueDelimiterMutatorConfig> {

    /**
     * Instantiates a new Key value delimiter field map mutator.
     *
     * @param mutatorConfig the mutator config
     */
    public KeyValueDelimiterFieldMapMutator(
            KeyValueDelimiterMutatorConfig mutatorConfig) {
        super(mutatorConfig);
    }

    /**
     * Instantiates a new Key value delimiter field map mutator.
     */
    public KeyValueDelimiterFieldMapMutator() {
        this(new KeyValueDelimiterMutatorConfig("KeyValueDelimiterMutator"));
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        try {
            return Arrays
                    .stream(getSplitPattern(mutatorConfig.getDelimiterRegex())
                            .split(targetString))
                    .map(getKeyValuePattern(
                            mutatorConfig.getKeyValueDelimiterRegex())::split)
                    .filter(this::checkThrowCondition)
                    .map(keyValue -> DelimiterFieldMapMutator
                            .applyDiscardRegex(mutatorConfig.getDiscardRegex(),
                                    keyValue))
                    .collect(Collectors.toMap(keyValue -> keyValue[0],
                            keyValue -> keyValue[1]));
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturn(log, e,
                    "buildFieldObjectMap", Collections::emptyMap,
                    mutatorConfig, targetString);
        }
    }

    private boolean checkThrowCondition(String[] strings) {
        return strings.length == 2 ? true :
                JMExceptionManager.throwRunTimeException("Wrong Key Value " +
                        "DELIMITER !!! - " + Arrays.asList(strings));
    }

    private Pattern getKeyValuePattern(String keyValueDelimiter) {
        return getSplitPattern(
                JMOptional.getOptional(keyValueDelimiter).orElse(
                        KeyValueDelimiterMutatorConfig.DefaultKeyValueDelimiterRegex));
    }

}
