package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.field.FilterConfig;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMStream;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@ToString
public class MatchFilter {
    private String mutateId;
    private Map<String, Pattern> filterPatternMap;
    private Map<String, Boolean> negateConfigMap;

    public MatchFilter(String mutatorId,
            Map<String, FilterConfig> filterConfigMap) {
        this.mutateId = mutatorId;
        this.filterPatternMap = JMStream.buildEntryStream(filterConfigMap)
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                        buildMatchRegex(entry.getKey(),
                                entry.getValue().getMatchRegex())))
                .filter(entry -> Objects.nonNull(entry.getValue())).collect(
                        Collectors
                                .toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.negateConfigMap =
                this.filterPatternMap.keySet().stream().collect(Collectors
                        .toMap(Function.identity(),
                                field -> filterConfigMap.get(field)
                                        .isNegate()));
    }

    private Pattern buildMatchRegex(String field, String matchRegex) {
        try {
            return Pattern.compile(matchRegex);
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnNull(log, e, "buildMatchRegex",
                            mutateId, field, matchRegex);
        }
    }

    public boolean filter(Map<String, Object> fieldMap) {
        return this.filterPatternMap.keySet().stream()
                .allMatch(field -> filter(fieldMap.get(field),
                        filterPatternMap.get(field),
                        negateConfigMap.get(field)));
    }


    public boolean filter(Object target, Pattern pattern, boolean negate) {
        return Optional.ofNullable(target).map(Object::toString)
                .map(pattern::matcher).map(Matcher::find)
                .filter(bool -> negate ? !bool : bool)
                .isPresent();
    }

}
