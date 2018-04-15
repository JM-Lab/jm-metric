package kr.jm.metric.data;

import kr.jm.metric.config.field.FieldConfig;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMOptional;

import java.util.*;

/**
 * The type Field map.
 */
@SuppressWarnings("ALL")
public class FieldMap implements Map<String, Object> {
    /**
     * The constant EMPTY_FIELD_MAP.
     */
    public static FieldMap EMPTY_FIELD_MAP =
            new FieldMap(Collections.emptyMap());
    private Map<String, Object> fieldMap;
    private Map<String, Object> dataMap;
    private Map<String, Object> metaMap;

    /**
     * Instantiates a new Field map.
     *
     * @param fieldMap the field map
     */
    public FieldMap(Map<String, Object> fieldMap) {
        this.fieldMap = JMMap.newFlatKeyMap(fieldMap);
    }

    /**
     * Extract field string map map.
     *
     * @return the map
     */
    public Map<String, String> extractFieldStringMap() {
        return JMMap.newChangedValueMap(getFieldMap(), Object::toString);
    }

    /**
     * Extract raw data string.
     *
     * @return the string
     */
    public String extractRawData() {
        return JMOptional.getOptional(getFieldMap(), FieldConfig.RAW_DATA)
                .map(Object::toString).orElse(null);
    }

    /**
     * Extract data map map.
     *
     * @return the map
     */
    public Map<String, Object> extractDataMap() {
        return JMLambda.supplierIfNull(this.dataMap,
                () -> this.dataMap = JMMap.newFilteredMap(getFieldMap(),
                        entry -> !isMeta(entry.getKey()) &&
                                !entry.getKey().equals(FieldConfig.RAW_DATA)));
    }

    /**
     * Extract meta map map.
     *
     * @return the map
     */
    public Map<String, Object> extractMetaMap() {
        return JMLambda.supplierIfNull(this.metaMap,
                () -> this.metaMap = JMMap.newFilteredMap(getFieldMap(),
                        entry -> isMeta(entry.getKey())));
    }

    private boolean isMeta(String field) {
        return field.startsWith(Transfer.META);
    }

    @Override
    public int size() {return getFieldMap().size();}

    private Map<String, Object> getFieldMap() {
        return Objects.requireNonNullElse(fieldMap, EMPTY_FIELD_MAP);
    }

    @Override
    public boolean isEmpty() {return getFieldMap().isEmpty();}

    @Override
    public boolean containsKey(Object key) {
        return getFieldMap().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getFieldMap().containsValue(value);
    }

    @Override
    public Object get(Object key) {return getFieldMap().get(key);}

    @Override
    public Object put(String key, Object value) {
        return getFieldMap().put(key, value);
    }

    @Override
    public Object remove(Object key) {return getFieldMap().remove(key);}

    @Override
    public void putAll(Map<? extends String, ?> m) {getFieldMap().putAll(m);}

    @Override
    public void clear() {getFieldMap().clear();}

    @Override
    public Set<String> keySet() {return getFieldMap().keySet();}

    @Override
    public Collection<Object> values() {return getFieldMap().values();}

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return getFieldMap().entrySet();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {return getFieldMap().equals(o);}

    @Override
    public int hashCode() {return getFieldMap().hashCode();}

    @Override
    public String toString() {
        return getFieldMap().toString();
    }

    /**
     * Empty field map.
     *
     * @return the field map
     */
    public static FieldMap empty() {
        return EMPTY_FIELD_MAP;
    }
}
