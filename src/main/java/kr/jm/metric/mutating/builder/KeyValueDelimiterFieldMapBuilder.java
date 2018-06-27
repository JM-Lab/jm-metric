package kr.jm.metric.mutating.builder;

import kr.jm.metric.config.mutating.KeyValueDelimiterMutatingConfig;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static kr.jm.metric.mutating.builder.DelimiterFieldMapBuilder.getSplitPattern;

/**
 * The type Key value delimiter field map builder.
 */
public class KeyValueDelimiterFieldMapBuilder extends
        AbstractFieldMapBuilder<KeyValueDelimiterMutatingConfig> {
    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(KeyValueDelimiterFieldMapBuilder.class);

    @Override
    public Map<String, Object> buildFieldObjectMap(
            KeyValueDelimiterMutatingConfig inputConfig, String targetString) {
        try {
            return Arrays
                    .stream(getSplitPattern(inputConfig.getDelimiterRegex())
                            .split(targetString))
                    .map(getKeyValuePattern(
                            inputConfig.getKeyValueDelimiterRegex())::split)
                    .filter(this::checkThrowCondition)
                    .map(keyValue -> DelimiterFieldMapBuilder
                            .applyDiscardRegex(inputConfig.getDiscardRegex(),
                                    keyValue))
                    .collect(Collectors.toMap(keyValue -> keyValue[0],
                            keyValue -> keyValue[1]));
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturn(log, e,
                    "buildFieldObjectMap", Collections::emptyMap,
                    inputConfig, targetString);
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
                        KeyValueDelimiterMutatingConfig.DefaultKeyValueDelimiterRegex));
    }

}
