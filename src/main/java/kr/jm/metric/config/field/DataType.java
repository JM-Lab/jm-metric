package kr.jm.metric.config.field;

import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.datastructure.JMMap;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMString;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

/**
 * The enum Data type.
 */
public enum DataType {
    /**
     * Word data type.
     */
    WORD,
    /**
     * Number data type.
     */
    NUMBER,
    /**
     * Na data type.
     */
    NA;

    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(DataType.class);
    private static final JMNestedMap<String, String, DataType>
            metricSourceTypeMapCache = new JMNestedMap<>();

    /**
     * Is number boolean.
     *
     * @param id     the id
     * @param field  the field
     * @param object the object
     * @return the boolean
     */
    public static boolean isNumber(String id, String field, Object object) {
        return getMetricSourceType(id, field, object).equals(NUMBER);
    }

    /**
     * Is word boolean.
     *
     * @param id     the id
     * @param field  the field
     * @param object the object
     * @return the boolean
     */
    public static boolean isWord(String id, String field, Object object) {
        return getMetricSourceType(id, field, object).equals(WORD);
    }

    /**
     * Gets metric source type.
     *
     * @param id     the id
     * @param field  the field
     * @param object the object
     * @return the metric source type
     */
    public static DataType getMetricSourceType(String id, String field,
            Object object) {
        return metricSourceTypeMapCache
                .getOrPutGetNew(id, field, () -> of(object));
    }

    /**
     * Put metric source type data type.
     *
     * @param id       the id
     * @param field    the field
     * @param dataType the data type
     * @return the data type
     */
    public static DataType putMetricSourceType(String id, String field,
            DataType dataType) {
        return metricSourceTypeMapCache.put(id, field, dataType);
    }

    /**
     * Gets or new metric data type map.
     *
     * @param id        the id
     * @param sampleMap the sample map
     * @return the or new metric data type map
     */
    public static Map<String, DataType> getOrNewMetricDataTypeMap(String id,
            Map<String, Object> sampleMap) {
        synchronized (metricSourceTypeMapCache) {
            return checkAndGetMetricDataTypeMap(id, sampleMap,
                    metricSourceTypeMapCache.getOrPutGetNew(id,
                            () -> buildDataTypeMap(sampleMap)));
        }
    }

    private static Map<String, DataType> buildDataTypeMap(
            Map<String, Object> sampleMap) {
        return JMMap.newChangedValueMap(sampleMap, DataType::of);
    }

    private static Map<String, DataType> checkAndGetMetricDataTypeMap(String id,
            Map<String, Object> sampleMap,
            Map<String, DataType> metricSourceTypeMap) {
        if (metricSourceTypeMap.size() < sampleMap.size()) {
            log.warn("MetricSourceTypeMap Update !!! - id = {}, oldSize = {}," +
                            " newSize = {}", id, metricSourceTypeMap.size(),
                    sampleMap.size());
            return JMMap.putGetNew(metricSourceTypeMapCache, id,
                    buildDataTypeMap(sampleMap));
        }
        return metricSourceTypeMap;
    }

    /**
     * Gets as opt.
     *
     * @param id the id
     * @return the as opt
     */
    public static Optional<Map<String, DataType>> getAsOpt(String id) {
        return JMOptional.getOptional(metricSourceTypeMapCache, id);
    }

    /**
     * Of data type.
     *
     * @param object the object
     * @return the data type
     */
    public static DataType of(Object object) {
        if (object instanceof Number)
            return NUMBER;
        if (object instanceof String && JMString.isWord((String) object))
            return WORD;
        return NA;
    }

}
