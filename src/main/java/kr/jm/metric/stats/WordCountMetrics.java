package kr.jm.metric.stats;

import kr.jm.utils.collections.JMCountMap;
import kr.jm.utils.collections.JMNestedMap;
import kr.jm.utils.helper.JMOptional;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Word count metrics.
 */
@ToString(callSuper = true)
public class WordCountMetrics extends JMNestedMap<String, String, Long> {

    /**
     * Instantiates a new Word count metrics.
     */
    public WordCountMetrics() {
        super();
    }

    /**
     * Instantiates a new Word count metrics.
     *
     * @param metricsMap the metrics map
     */
    public WordCountMetrics(Map<String, Map<String, Long>> metricsMap) {
        super(metricsMap);
    }

    /**
     * Merge all word count metrics.
     *
     * @param numberListMapList the number list map list
     * @return the word count metrics
     */
    public static WordCountMetrics mergeAll(
            List<WordCountMetrics> numberListMapList) {
        WordCountMetrics wordCountMetrics = new WordCountMetrics();
        numberListMapList.forEach(wordCountMetrics::merge);
        return wordCountMetrics;
    }

    /**
     * Merge.
     *
     * @param key      the key
     * @param countMap the count map
     */
    public void merge(String key, Map<String, Long> countMap) {
        countMap.forEach((word, count)
                -> put(key, word, count + getCount(key, word)));
    }

    /**
     * Merge word count metrics.
     *
     * @param wordCountMetrics the word count metrics
     * @return the word count metrics
     */
    public WordCountMetrics merge(WordCountMetrics wordCountMetrics) {
        wordCountMetrics.forEach(this::merge);
        return this;
    }

    /**
     * Gets count.
     *
     * @param key  the key
     * @param word the word
     * @return the count
     */
    public long getCount(String key, String word) {
        return Optional.ofNullable(get(key, word)).orElse(0L);
    }

    /**
     * Gets count map.
     *
     * @param key the key
     * @return the count map
     */
    public JMCountMap<String> getCountMap(String key) {
        return new JMCountMap<>(JMOptional.getOptional(this, key)
                .orElseGet(Collections::emptyMap));
    }

}
