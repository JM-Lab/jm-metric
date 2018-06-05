package kr.jm.metric.processor;

import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.DelimiterMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.config.mutating.field.FieldConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.metric.stats.WordCountNumberStatsMetrics;
import kr.jm.metric.stats.WordCountNumberStatsMetricsBuilder;
import kr.jm.metric.stats.collector.MetricWordNumberCollector;
import kr.jm.metric.transformer.FieldMapListConfigIdTransferListTransformer;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMResources;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WordNumberStatsConfigFieldMapConfigIdTransferListTransformProcessorTest {
    private static final String FileName = "webAccessLogSample.txt";
    private static final String ConfigId = "webAccessLogTest";
    private FieldMapListConfigIdTransferListTransformer
            fieldMapListConfigIdDataTransferListTransformer;

    @Before
    public void setUp() {
        Map<String, MutatingConfig> requestFieldFormat =
                Map.of("request", new DelimiterMutatingConfig(null,
                        JMArrays.buildArray("requestMethod",
                                "requestedUrl", "requestProtocol")));
        FieldConfig fieldConfig =
                new FieldConfig(requestFieldFormat, true, null, null, null,
                        null, null, null);
        MutatingConfig config =
                new ApacheAccessLogMutatingConfig(ConfigId, fieldConfig,
                        "%h %l %u %t \"%r\" %>s %b " + "\"%{Referer}i\" " +
                                "\"%{User-agent}i\" %D")
                        .withBindDataIds(FileName);
        System.out.println(JMJson.toJsonString(config));
        MutatingConfigManager mutatingConfigManager =
                new MutatingConfigManager(List.of(config));
        this.fieldMapListConfigIdDataTransferListTransformer =
                new FieldMapListConfigIdTransferListTransformer(
                        mutatingConfigManager);
    }

    @Test
    public void testAccessLogProcessor() {
        List<ConfigIdTransfer<List<FieldMap>>> configIdListDataTransferMap =
                fieldMapListConfigIdDataTransferListTransformer
                        .apply(new Transfer<>(FileName,
                                JMResources.readLines(FileName)));
        assertEquals(1, configIdListDataTransferMap.size());
        Transfer<List<FieldMap>> listTransfer =
                configIdListDataTransferMap.get(0);
        System.out.println(listTransfer);
        MetricWordNumberCollector wordNumberCollector =
                new MetricWordNumberCollector(listTransfer.getDataId(),
                        listTransfer
                                .getTimestamp(), listTransfer.getMeta());
        wordNumberCollector.addFieldMapList(listTransfer.getData());
        System.out.println(JMJson.toJsonString(wordNumberCollector));

        Optional<WordCountNumberStatsMetrics> wordCountNumberStatsMetricsAsOpt =
                WordCountNumberStatsMetricsBuilder.of().add(wordNumberCollector)
                        .buildMetricAsOpt(FileName);
        WordCountNumberStatsMetrics dataObject =
                wordCountNumberStatsMetricsAsOpt.get();
        System.out.println(JMJson.toJsonString(dataObject));
        assertTrue(wordCountNumberStatsMetricsAsOpt.isPresent());
        Optional<WordCountNumberStatsMetrics>
                wordCountNumberStatsMetricsAsOpt2 =
                WordCountNumberStatsMetricsBuilder.of().add(wordNumberCollector)
                        .buildMetricAsOpt(FileName);
        System.out.println(JMJson.toJsonString
                (wordCountNumberStatsMetricsAsOpt2.get()));
        assertTrue(wordCountNumberStatsMetricsAsOpt2.isPresent());

        assertEquals(
                wordCountNumberStatsMetricsAsOpt.get().getNumberStatsMetrics
                        ().values().stream().map(map -> map.get("sum"))
                        .mapToLong(Number::longValue).sum() * 2,
                wordCountNumberStatsMetricsAsOpt2.get().getNumberStatsMetrics()
                        .values().stream().map(map -> map.get("sum"))
                        .mapToLong(Number::longValue).sum());
    }
}