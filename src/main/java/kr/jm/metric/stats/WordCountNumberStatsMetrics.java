package kr.jm.metric.stats;

import kr.jm.utils.stats.collector.WordNumberCollector;
import lombok.ToString;

import java.util.Map;

/**
 * The type Word count number stats metrics.
 */
@ToString(callSuper = true)
public class WordCountNumberStatsMetrics {

    private String collectorId;
    private long timestamp;
    private Map<String, Object> metaMap;
    private WordCountMetrics wordCountMetrics;
    private NumberStatsMetrics numberStatsMetrics;

    /**
     * Instantiates a new Word count number stats metrics.
     *
     * @param wordNumberCollector the word number collector
     */
    public WordCountNumberStatsMetrics(
            WordNumberCollector wordNumberCollector) {
        this.collectorId = wordNumberCollector
                .getCollectorId();
        this.timestamp = wordNumberCollector.getTimestamp();
        this.metaMap = wordNumberCollector.getMetaMap();
        this.wordCountMetrics = new WordCountMetrics(
                wordNumberCollector.buildWordCountMetricsMap());
        this.numberStatsMetrics = new NumberStatsMetrics(wordNumberCollector
                .buildNumberStatsMetricsMap());
    }

    private WordCountNumberStatsMetrics() {}

    /**
     * Gets collector id.
     *
     * @return the collector id
     */
    public String getCollectorId() {return this.collectorId;}

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {return this.timestamp;}

    /**
     * Gets meta map.
     *
     * @return the meta map
     */
    public Map<String, Object> getMetaMap() {return this.metaMap;}

    /**
     * Gets word count metrics.
     *
     * @return the word count metrics
     */
    public WordCountMetrics getWordCountMetrics() {return this.wordCountMetrics;}

    /**
     * Gets number stats metrics.
     *
     * @return the number stats metrics
     */
    public NumberStatsMetrics getNumberStatsMetrics() {return this.numberStatsMetrics;}

}
