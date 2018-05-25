package kr.jm.metric.transformer;

import kr.jm.metric.config.*;
import kr.jm.metric.config.field.CombinedFieldConfig;
import kr.jm.metric.config.field.DataType;
import kr.jm.metric.config.field.FieldConfig;
import kr.jm.metric.data.ConfigIdTransfer;
import kr.jm.metric.data.FieldMap;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMString;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class FieldMapListConfigIdTransferListTransformerTest {
    private static final String FileName = "webAccessLogSample.txt";
    private static final String ConfigId = "webAccessLogTest";
    private FieldMapListConfigIdTransferListTransformer
            fieldMapListConfigIdDataTransferListTransformer;
    private MetricConfigManager metricConfigManager;

    @Before
    public void setUp() {
        Map<String, MetricConfig> requestFieldFormat =
                Map.of("request", new DelimiterMetricConfig(null,
                        JMArrays.buildArray("requestMethod",
                                "requestUrl", "requestProtocol")));
        List<String> ignoreFieldList = List.of("remoteLogName", "remoteUser");
        Map<String, DataType> fieldDataTypeMap = Map.of
                ("requestTime", DataType.valueOf("NUMBER"), "sizeByte",
                        DataType.valueOf("NUMBER"));
        CombinedFieldConfig[] combinedFieldConfigs = {new CombinedFieldConfig(
                JMArrays.buildArray("remoteHost", "requestUrl"),
                "combinedField", null)};
        FieldConfig fieldConfig =
                new FieldConfig(requestFieldFormat, true, ignoreFieldList,
                        combinedFieldConfigs, fieldDataTypeMap, null, null,
                        null);
        MetricConfig config =
                new ApacheAccessLogMetricConfig(ConfigId, fieldConfig,
                        "%h %l %u %t \"%r\" %>s %b " + "\"%{Referer}i\" " +
                                "\"%{User-agent}i\" %D")
                        .withBindDataIds(FileName);
        System.out.println(JMJson.toJsonString(config));
        this.metricConfigManager = new MetricConfigManager(List.of(config));
        this.fieldMapListConfigIdDataTransferListTransformer =
                new FieldMapListConfigIdTransferListTransformer(
                        metricConfigManager);
    }

    @Test
    public void testAccessLogTransformer() {
        List<String> readLineList = JMResources.readLines(FileName);
        List<ConfigIdTransfer<List<FieldMap>>> listDataTransferList =
                fieldMapListConfigIdDataTransferListTransformer
                        .apply(new Transfer<>(FileName, readLineList));
        System.out.println(listDataTransferList);
        assertEquals(1, listDataTransferList.size());
        Transfer<List<FieldMap>> listTransfer =
                listDataTransferList.get(0);
        assertEquals(readLineList.size(), listTransfer.getData().size());
        Map<String, Object> sampleMap = listTransfer.getData().get(0);
        System.out.println(JMJson.toJsonString(sampleMap));
        assertTrue(sampleMap.containsKey("requestMethod"));
        assertTrue(sampleMap.containsKey("requestUrl"));
        assertFalse(sampleMap.containsKey("remoteLogName"));
        assertTrue(sampleMap.get("requestTime") instanceof Number);
        assertTrue(sampleMap.get("sizeByte") instanceof Number);
        assertTrue(JMString.isWord(sampleMap.get("statusCode").toString()));
        assertEquals("10.10.78.35_/healthcheck.jsp",
                sampleMap.get("combinedField"));
        System.out.println(listTransfer.getMeta());
        assertNotNull(listTransfer.getMeta());
    }

    @Test
    public void testJsonListTransformer() {
        FieldConfig fieldConfig = JMJson.withJsonString(
                "{\"dateFormat\":{\"time\":{\"dateFormatType\":\"EPOCH\", " +
                        "\"timeUnit\": \"SECONDS\"," +
                        "\"changeDateFormat\":{\"dateFormatType\":\"ISO\"}}}}",
                FieldConfig.class);
        System.out.println(JMJson.toJsonString(fieldConfig));
        this.metricConfigManager
                .insertConfigList(List.of(new JsonMetricConfig("JSON",
                        fieldConfig, true)));
        this.metricConfigManager.bindDataIdToConfigId("jsonList", "JSON");

        String targetString =
                "[{\"values\":[204,206],\"dstypes\":[\"derive\",\"derive\"]," +
                        "\"dsnames\":[\"rx\",\"tx\"],\"time\":1523355042.220," +
                        "\"interval\":5.000,\"host\":\"jm-macbook-pro-6.local\",\"plugin\":\"interface\",\"plugin_instance\":\"en5\",\"type\":\"if_packets\",\"type_instance\":\"\"},{\"values\":[0,0],\"dstypes\":[\"derive\",\"derive\"],\"dsnames\":[\"rx\",\"tx\"],\"time\":1523355042.220,\"interval\":5.000,\"host\":\"jm-macbook-pro-6.local\",\"plugin\":\"interface\",\"plugin_instance\":\"en5\",\"type\":\"if_errors\",\"type_instance\":\"\"}]";
        List<ConfigIdTransfer<List<FieldMap>>> listDataTransferList =
                fieldMapListConfigIdDataTransferListTransformer
                        .apply(new Transfer<>("jsonList",
                                List.of(targetString)));
        System.out.println(listDataTransferList);
        assertEquals(1, listDataTransferList.size());
        Transfer<List<FieldMap>> listTransfer = listDataTransferList.get(0);
        assertEquals(2, listTransfer.getData().size());
        Map<String, Object> sampleMap = listTransfer.getData().get(0);
        System.out.println(JMJson.toJsonString(sampleMap));
        assertTrue(sampleMap.containsKey("dsnames"));
        assertTrue(sampleMap.containsKey("plugin_instance"));
        assertTrue(sampleMap.get("interval") instanceof Number);
        assertTrue(JMString.isWord(sampleMap.get("host").toString()));
        assertEquals("interface", sampleMap.get("plugin"));
        System.out.println(listTransfer.getMeta());
        assertNotNull(listTransfer.getMeta());
        assertEquals("2018-04-10T10:10:42.000Z", sampleMap.get("time"));
    }
}