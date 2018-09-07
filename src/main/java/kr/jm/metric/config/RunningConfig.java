package kr.jm.metric.config;

import kr.jm.utils.helper.JMOptional;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class RunningConfig extends AbstractConfig {
    private Map<String, Object> input;
    private Map<String, Object> mutator;
    private Map<String, Object>[] outputs;

    public Optional<String> getInputIdAsOpt() {
        return JMOptional.getOptional(input, "inputId").map(Object::toString);
    }

    public Optional<String> getMutatorIdAsOpt() {
        return JMOptional.getOptional(mutator, "mutatorId")
                .map(Object::toString);
    }

    public Map<String, Map<String, Object>> getOutputIdMap() {
        return JMOptional.getOptional(outputs).stream().flatMap(Arrays::stream)
                .map(map -> new AbstractMap.SimpleEntry<>(map.get("outputId"),
                        map)).filter(entry -> Objects.nonNull(entry.getKey()))
                .collect(Collectors.toMap(entry -> entry.getKey().toString(),
                        entry -> entry.getValue()));
    }
}
