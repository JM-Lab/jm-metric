package kr.jm.metric.mutating.builder;

import kr.jm.metric.config.mutating.DelimiterMutatingConfig;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPredicate;
import kr.jm.utils.helper.JMStream;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The type Delimiter field map builder.
 */
public class DelimiterFieldMapBuilder extends
        AbstractFieldMapBuilder<DelimiterMutatingConfig> {

    /**
     * The constant DefaultIndexHeader.
     */
    public static final String DefaultIndexHeader = "Index-";
    private static Map<String, Pattern> SplitPatternCache =
            new HashMap<>() {{
                Pattern defaultSplitPattern = Pattern.compile("\\s+");
                put(null, defaultSplitPattern);
                put("", defaultSplitPattern);
            }};
    private static Map<String, Pattern> discardPatternCache = new HashMap<>();
    private static Map<Integer, String[]> defaultFieldsCache =
            new HashMap<>();

    @Override
    public Map<String, Object> buildFieldObjectMap(
            DelimiterMutatingConfig inputConfig, String targetString) {
        return buildFieldObjectMap(inputConfig, targetString,
                getSplitPattern(inputConfig.getDelimiterRegex()));
    }

    /**
     * Gets split pattern.
     *
     * @param delimiterRegex the delimiter regex
     * @return the split pattern
     */
    public static Pattern getSplitPattern(String delimiterRegex) {
        return JMMap.getOrPutGetNew(SplitPatternCache, delimiterRegex,
                () -> Pattern.compile(delimiterRegex));
    }

    private Map<String, Object> buildFieldObjectMap(
            DelimiterMutatingConfig inputConfig, String targetString,
            Pattern pattern) {
        return buildFieldObjectMap(inputConfig, targetString, pattern,
                pattern.split(targetString));
    }

    private Map<String, Object> buildFieldObjectMap(
            DelimiterMutatingConfig inputConfig, String targetString,
            Pattern pattern, String[] splitValues) {
        setDefaultFieldsIfEmpty(inputConfig, splitValues);
        return buildFieldObjectMap(inputConfig.getFields(),
                applyDiscardRegex(inputConfig.getDiscardRegex(), splitValues),
                pattern, targetString);
    }

    private void setDefaultFieldsIfEmpty(DelimiterMutatingConfig inputConfig,
            String[] splitValues) {
        if (!JMOptional.getOptional(inputConfig.getFields()).isPresent())
            inputConfig.setFields(getDefaultFields(splitValues.length));
    }

    /**
     * Get default fields string [ ].
     *
     * @param length the length
     * @return the string [ ]
     */
    public static String[] getDefaultFields(int length) {
        return JMMap.getOrPutGetNew(defaultFieldsCache, length,
                () -> JMArrays.toArray(JMStream.increaseRange(length)
                        .mapToObj(i -> DefaultIndexHeader + i)
                        .collect(Collectors.toList())));
    }

    /**
     * Apply discard regex string [ ].
     *
     * @param discardRegex the discard regex
     * @param values       the values
     * @return the string [ ]
     */
    public static String[] applyDiscardRegex(String discardRegex,
            String[] values) {
        return JMOptional.getOptional(discardRegex)
                .map(DelimiterFieldMapBuilder::getDiscardPattern)
                .map(discardPattern -> applyDiscardRegex(discardPattern,
                        values)).orElse(values);
    }

    private static String[] applyDiscardRegex(Pattern discardPattern,
            String[] strings) {
        return JMStream.increaseRange(strings.length)
                .mapToObj(i -> removeAll(discardPattern, strings[i]))
                .collect(Collectors.toList()).toArray(strings);
    }

    /**
     * Gets discard pattern.
     *
     * @param regex the regex
     * @return the discard pattern
     */
    public static Pattern getDiscardPattern(String regex) {
        return JMMap.getOrPutGetNew(discardPatternCache, regex,
                () -> Pattern.compile(regex));
    }

    private static String removeAll(Pattern discardPattern,
            String keyValueString) {
        return discardPattern.matcher(keyValueString).replaceAll("");
    }

    private Map<String, Object> buildFieldObjectMap(String[] fields,
            String[] splitValues, Pattern pattern, String targetString) {
        //noinspection unchecked
        return Optional.of(JMOptional
                .getListIfIsPresent(JMOptional.getOptional(fields),
                        JMOptional.getOptional(splitValues)))
                .filter(JMPredicate.getEqualSize(2))
                .map(list -> JMMap.newCombinedMap(list.get(0), list.get(1)))
                .map(map -> JMMap.newFilteredChangedValueMap(map,
                        entry -> Objects.nonNull(entry.getValue()),
                        v -> (Object) v)).orElseGet(() -> JMExceptionManager
                        .handleExceptionAndReturn(log, JMExceptionManager
                                        .newRunTimeException("Wrong Input Format !!!"),
                                "buildFieldObjectMap", Collections::emptyMap,
                                Arrays.toString(fields),
                                Arrays.toString(splitValues), pattern,
                                targetString));
    }

}
