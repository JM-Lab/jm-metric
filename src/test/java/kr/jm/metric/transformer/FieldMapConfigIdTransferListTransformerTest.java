package kr.jm.metric.transformer;

import kr.jm.metric.config.ApacheAccessLogMetricConfig;
import kr.jm.metric.config.MetricConfig;
import kr.jm.metric.config.MetricConfigManager;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMResources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class FieldMapConfigIdTransferListTransformerTest {
    private static final String FileName = "webAccessLogSample.txt";
    private FieldMapConfigIdTransferListTransformer
            fieldMapConfigIdDataTransferListTransformer;
    private FieldMapListConfigIdTransferListTransformer
            fieldMapListConfigIdDataTransferListTransformer;
    private static final String ConfigId = "apacheCommonLogTest";
    private List<String> lineList;


    @Before
    public void setUp() {
        this.lineList = JMResources.readLines(FileName);
        MetricConfig config = new ApacheAccessLogMetricConfig
                (ConfigId, "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" " +
                        "\"%{User-agent}i\" %D").withBindDataIds(FileName);
        MetricConfigManager metricConfigManager =
                new MetricConfigManager(List.of(config));
        this.fieldMapConfigIdDataTransferListTransformer =
                new FieldMapConfigIdTransferListTransformer(
                        metricConfigManager);
        this.fieldMapListConfigIdDataTransferListTransformer = new
                FieldMapListConfigIdTransferListTransformer(
                metricConfigManager);
    }

    @Test
    public void testTransform() {
        Stream<List<ConfigIdTransfer<FieldMap>>>
                configIdFieldMapDataTransferMapStream =
                lineList.stream().map(log -> new Transfer(FileName, log))
                        .map(dataTransfer -> fieldMapConfigIdDataTransferListTransformer
                                .apply(dataTransfer));
        List<Transfer<FieldMap>> transferMap =
                configIdFieldMapDataTransferMapStream
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        System.out.println(transferMap);

        List<ConfigIdTransfer<List<FieldMap>>> dataTransferListMap =
                fieldMapListConfigIdDataTransferListTransformer
                        .apply(new Transfer<>(FileName, lineList));
        assertEquals(1, dataTransferListMap.size());
        Transfer<List<FieldMap>> listTransfer =
                dataTransferListMap.get(0);
        System.out.println(listTransfer);

        List<Map<String, Object>> collect =
                transferMap.stream().map(Transfer::getData)
                        .collect(Collectors.toList());
        Assert.assertEquals(collect.size(), listTransfer.getData().size());
        Assert.assertEquals(collect.toString(),
                listTransfer.getData().toString());
    }

}