package kr.jm.metric.stats.collector;

import kr.jm.metric.config.field.DataType;
import kr.jm.metric.data.FieldMap;
import kr.jm.utils.stats.collector.WordNumberCollector;

import java.util.List;
import java.util.Map;

/**
 * The type Metric word number collector.
 */
public class MetricWordNumberCollector extends WordNumberCollector {

    /**
     * Instantiates a new Metric word number collector.
     *
     * @param collectorId the collector id
     */
    public MetricWordNumberCollector(String collectorId) {
        this(collectorId, System.currentTimeMillis());
    }

    /**
     * Instantiates a new Metric word number collector.
     *
     * @param collectorId the collector id
     * @param timestamp   the timestamp
     */
    public MetricWordNumberCollector(String collectorId, long timestamp) {
        this(collectorId, timestamp, null);
    }

    /**
     * Instantiates a new Metric word number collector.
     *
     * @param collectorId the collector id
     * @param timestamp   the timestamp
     * @param metaMap     the meta map
     */
    public MetricWordNumberCollector(String collectorId, long timestamp,
            Map<String, Object> metaMap) {
        super(collectorId, timestamp, metaMap);
    }

    /**
     * Add field map metric word number collector.
     *
     * @param fieldMap the field map
     * @return the metric word number collector
     */
    public MetricWordNumberCollector addFieldMap(FieldMap fieldMap) {
        addFieldMap(
                DataType.getOrNewMetricDataTypeMap(getCollectorId(), fieldMap),
                fieldMap);
        return this;
    }

    private MetricWordNumberCollector addFieldMap(
            Map<String, DataType> metricDataTypeMap,
            Map<String, Object> fieldMap) {
        fieldMap.forEach(
                (key, data) -> addData(metricDataTypeMap.get(key), key, data));
        return this;
    }

    /**
     * Add field map list metric word number collector.
     *
     * @param fieldMapList the field map list
     * @return the metric word number collector
     */
    public MetricWordNumberCollector addFieldMapList(
            List<FieldMap> fieldMapList) {
        for (FieldMap fieldMap : fieldMapList)
            addFieldMap(fieldMap);
        return this;
    }


    private MetricWordNumberCollector addData(DataType dataType, String key,
            Object data) {
        switch (dataType) {
            case NUMBER:
                addNumber(key, (Number) data);
                break;
            case WORD:
                addWord(key, (String) data);
                break;
        }
        return this;
    }


}
