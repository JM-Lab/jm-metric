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

/**
 * The type Field meta.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FieldMeta {

    /**
     * The Unit.
     */
    @Getter
    protected Map<String, String> unit;
    /**
     * The Custom.
     */
    @Getter
    protected Map<String, Object> custom;

    private Map<String, Object> fieldMetaMap;

    /**
     * Instantiates a new Field meta.
     *
     * @param unit   the unit
     * @param custom the custom
     */
    public FieldMeta(
            Map<String, String> unit,
            Map<String, Object> custom) {
        this.unit = unit;
        this.custom = custom;
    }

    /**
     * Extract field meta map map.
     *
     * @return the map
     */
    synchronized public Map<String, Object> extractFieldMetaMap() {
        return Objects.requireNonNullElseGet(this.fieldMetaMap,
                () -> this.fieldMetaMap = buildFlatMap(new HashMap<>()));
    }

    private Map<String, Object> buildFlatMap(Map<String, Object> flatMap) {
        buildFlatMap(flatMap, "unit", unit);
        buildFlatMap(flatMap, "custom", custom);
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
