package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.DelimiterMutatorConfig;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPredicate;
import kr.jm.utils.helper.JMStream;
import lombok.ToString;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The type Delimiter field map mutator.
 */
@ToString(callSuper = true)
public class DelimiterFieldMapMutator extends
        AbstractFieldMapMutator<DelimiterMutatorConfig> {

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

    /**
     * Instantiates a new Delimiter field map mutator.
     *
     * @param mutatorConfig the mutator config
     */
    public DelimiterFieldMapMutator(DelimiterMutatorConfig mutatorConfig) {
        super(mutatorConfig);
    }

    /**
     * Instantiates a new Delimiter field map mutator.
     */
    public DelimiterFieldMapMutator() {
        this(new DelimiterMutatorConfig("DelimiterMutator"));
    }

    @Override
    public Map<String, Object> buildFieldObjectMap(String targetString) {
        return buildFieldObjectMap(targetString,
                getSplitPattern(mutatorConfig.getDelimiterRegex()));
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

    private Map<String, Object> buildFieldObjectMap(String targetString,
            Pattern pattern) {
        return buildFieldObjectMap(targetString, pattern,
                pattern.split(targetString));
    }

    private Map<String, Object> buildFieldObjectMap(String targetString,
            Pattern pattern, String[] splitValues) {
        setDefaultFieldsIfEmpty(splitValues);
        return buildFieldObjectMap(mutatorConfig.getFields(),
                applyDiscardRegex(mutatorConfig.getDiscardRegex(), splitValues),
                pattern, targetString);
    }

    private void setDefaultFieldsIfEmpty(String[] splitValues) {
        if (!JMOptional.getOptional(mutatorConfig.getFields()).isPresent())
            mutatorConfig.setFields(getDefaultFields(splitValues.length));
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
                .map(DelimiterFieldMapMutator::getDiscardPattern)
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
