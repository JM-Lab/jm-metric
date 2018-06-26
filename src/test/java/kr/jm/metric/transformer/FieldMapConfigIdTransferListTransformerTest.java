package kr.jm.metric.transformer;

import kr.jm.metric.config.mutating.ApacheAccessLogMutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfig;
import kr.jm.metric.config.mutating.MutatingConfigManager;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMResources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldMapConfigIdTransferListTransformerTest {
    private static final String FileName = "webAccessLogSample.txt";
    private FieldMapConfigIdTransferListTransformer
            fieldMapConfigIdDataTransferListTransformer;
    private static final String ConfigId = "apacheCommonLogTest";
    private List<String> lineList;


    @Before
    public void setUp() {
        this.lineList = JMResources.readLines(FileName);
        MutatingConfig config = new ApacheAccessLogMutatingConfig
                (ConfigId, "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" " +
                        "\"%{User-agent}i\" %D");
        MutatingConfigManager mutatingConfigManager =
                new MutatingConfigManager(List.of(config));
        this.fieldMapConfigIdDataTransferListTransformer =
                new FieldMapConfigIdTransferListTransformer(
                        mutatingConfigManager.getConfig(ConfigId));
    }

    @Test
    public void testTransform() {
        List<Transfer<String>> transferList = lineList.stream()
                .map(log -> new Transfer<>(FileName, log))
                .collect(Collectors.toList());
        List<ConfigIdTransfer<FieldMap>> configIdTransferList =
                fieldMapConfigIdDataTransferListTransformer
                        .apply(transferList);
        System.out.println(configIdTransferList);

        List<Map<String, Object>> collect =
                configIdTransferList.stream().map(Transfer::getData)
                        .collect(Collectors.toList());
        Assert.assertEquals(1024, collect.size());

    }

}