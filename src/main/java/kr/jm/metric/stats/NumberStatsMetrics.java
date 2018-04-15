package kr.jm.metric.stats;

import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.stats.StatsMap;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * The type Number stats metrics.
 */
@ToString(callSuper = true)
public class NumberStatsMetrics extends JMNestedMap<String, String, Number> {

    /**
     * Instantiates a new Number stats metrics.
     */
    public NumberStatsMetrics() {
        super();
    }

    /**
     * Instantiates a new Number stats metrics.
     *
     * @param metricsMap the metrics map
     */
    public NumberStatsMetrics(Map<String, Map<String, Number>> metricsMap) {
        super(metricsMap);
    }


    /**
     * Merge.
     *
     * @param key                       the key
     * @param statsFieldStringNumberMap the stats field string number map
     */
    public void merge(String key,
            Map<String, Number> statsFieldStringNumberMap) {
        put(key, getStatsMap(key).merge(StatsMap.changeIntoStatsMap
                (statsFieldStringNumberMap)).getStatsFieldStringMap());
    }

    /**
     * Merge number stats metrics.
     *
     * @param statsNumberMap the stats number map
     * @return the number stats metrics
     */
    public NumberStatsMetrics merge(NumberStatsMetrics statsNumberMap) {
        statsNumberMap.forEach(this::merge);
        return this;
    }

    /**
     * Merge all number stats metrics.
     *
     * @param numberStatsMetricsList the number stats metrics list
     * @return the number stats metrics
     */
    public static NumberStatsMetrics mergeAll(
            List<NumberStatsMetrics> numberStatsMetricsList) {
        NumberStatsMetrics numberStatsMetrics = new NumberStatsMetrics();
        numberStatsMetricsList.forEach(numberStatsMetrics::merge);
        return numberStatsMetrics;
    }

    /**
     * Gets stats map.
     *
     * @param key the key
     * @return the stats map
     */
    public StatsMap getStatsMap(String key) {
        return StatsMap.changeIntoStatsMap(get(key));
    }

}
