package kr.jm.metric.config.mutator.field;

import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMOptional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FieldMeta {

    @Getter
    protected Map<String, String> unit;

    private Map<String, Object> fieldMetaMap;

    public FieldMeta(Map<String, String> unit) {
        this.unit = unit;
    }

    synchronized public Map<String, Object> extractFieldMetaMap() {
        return Objects.requireNonNullElseGet(this.fieldMetaMap,
                () -> this.fieldMetaMap = buildFlatMap(new HashMap<>()));
    }

    private Map<String, Object> buildFlatMap(Map<String, Object> flatMap) {
        buildFlatMap(flatMap, "unit", unit);
        return flatMap;
    }

    private void buildFlatMap(Map<String, Object> flatMap,
            String field, Map<String, ?> nestedMap) {
        JMOptional.getOptional(nestedMap).map(map -> removedNullMap(field, map))
                .ifPresent(map -> flatMap.put(field, map));
    }

    private Map<String, ?> removedNullMap(String field, Map<String, ?> map) {
        return JMMap.newFilteredChangedValueWithEntryMap(map, entry ->
                        checkNull(field, entry.getKey(), entry.getValue()),
                Map.Entry::getValue);
    }

    private boolean checkNull(String field, String key, Object value) {
        return Objects.nonNull(value) || JMExceptionManager
                .handleExceptionAndReturnFalse(log,
                        new NullPointerException("value"), "checkNull", field,
                        key, null);
    }
}
