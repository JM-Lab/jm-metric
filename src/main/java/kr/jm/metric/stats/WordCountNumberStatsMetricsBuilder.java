package kr.jm.metric.stats;

import kr.jm.utils.collections.JMListMap;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPredicate;
import kr.jm.utils.stats.collector.WordNumberCollector;

import java.util.List;
import java.util.Optional;

/**
 * The type Word count number stats metrics builder.
 */
public class WordCountNumberStatsMetricsBuilder {

    private static WordCountNumberStatsMetricsBuilder
            wordCountNumberStatsMetricsBuilder;
    private JMListMap<String, WordNumberCollector> wordNumberCollectorListMap;

    /**
     * Of word count number stats metrics builder.
     *
     * @return the word count number stats metrics builder
     */
    public static WordCountNumberStatsMetricsBuilder of() {
        return JMLambda.supplierIfNull(wordCountNumberStatsMetricsBuilder,
                () -> wordCountNumberStatsMetricsBuilder =
                        new WordCountNumberStatsMetricsBuilder());
    }

    private WordCountNumberStatsMetricsBuilder() {
        this.wordNumberCollectorListMap = new JMListMap<>();
    }

    /**
     * Add word count number stats metrics builder.
     *
     * @param wordNumberCollector the word number collector
     * @return the word count number stats metrics builder
     */
    public WordCountNumberStatsMetricsBuilder add(
            WordNumberCollector wordNumberCollector) {
        wordNumberCollectorListMap.add(wordNumberCollector.getCollectorId(),
                wordNumberCollector);
        return this;
    }

    /**
     * Build metric as opt optional.
     *
     * @param collectorId the collector id
     * @return the optional
     */
    public Optional<WordCountNumberStatsMetrics> buildMetricAsOpt(
            String collectorId) {
        return JMOptional.getOptional(wordNumberCollectorListMap, collectorId)
                .filter(JMPredicate.getGreaterSize(0))
                .map(list -> buildMetric(list.get(0), list));
    }

    private WordCountNumberStatsMetrics buildMetric(
            WordNumberCollector sampleWordNumberCollector,
            List<WordNumberCollector> wordNumberCollectorList) {
        return new WordCountNumberStatsMetrics(
                new WordNumberCollector(
                        sampleWordNumberCollector.getCollectorId(),
                        sampleWordNumberCollector.getTimestamp(),
                        sampleWordNumberCollector.getMetaMap())
                        .mergeAll(wordNumberCollectorList));
    }

}
