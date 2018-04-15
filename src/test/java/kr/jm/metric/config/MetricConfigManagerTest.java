package kr.jm.metric.config;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMJson;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MetricConfigManagerTest {

    private MetricConfigManager metricConfigManager;

    @Before
    public void setUp() throws Exception {
        this.metricConfigManager = new MetricConfigManager();
    }

    @Test
    public void insertConfigDelimiter() {
        String configId = "delimiterSample";
        MetricConfig config = null;
        try {
            config = new DelimiterMetricConfig(configId,
                    JMArrays.buildArray("field1", "field1"));
        } catch (Exception e) {
            assertEquals(e.getMessage(),
                    "Occur Duplicate field !!! - [field1, field1]");
        }
        config =
                new DelimiterMetricConfig(configId,
                        JMArrays.buildArray("field1", "field2"));
        insertConfig(config);

        System.out.println(metricConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(metricConfigManager.getConfigList()));
        assertNotNull(metricConfigManager.getConfig(configId));
        assertEquals(1, metricConfigManager.getConfigList().size());
    }

    @Test
    public void insertConfigKeyValueDelimiter() {
        String configId = "keyValueDelimiterSample";
        MetricConfig config =
                new KeyValueDelimiterMetricConfig("keyValueDelimiterSample",
                        null, "=", ":",
                        "[{}\", ]");
        insertConfig(config);

        System.out.println(metricConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(metricConfigManager.getConfigList()));
        assertNotNull(metricConfigManager.getConfig(configId));
        assertEquals(1, metricConfigManager.getConfigList().size());
    }

    @Test
    public void insertConfigFormatted() {
        String configId = "formattedSample";
        MetricConfig config =
                new FormattedMetricConfig(configId, "$ip - - $url",
                        Map.of("$ip", "ip", "$url",
                                "url"));
        insertConfig(config);

        System.out.println(metricConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(metricConfigManager.getConfigList()));
        assertNotNull(metricConfigManager.getConfig(configId));
        assertEquals(1, metricConfigManager.getConfigList().size());
    }

    @Test
    public void insertConfigApacheAccessLog() {
        String configId = "apacheAccessLogSample";
        String apacheCommonLogFormat =
                "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"";
        MetricConfig config =
                new ApacheAccessLogMetricConfig(configId, apacheCommonLogFormat);
        insertConfig(config);

        System.out.println(metricConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(metricConfigManager.getConfigList()));
        assertNotNull(metricConfigManager.getConfig(configId));
        assertEquals(1, metricConfigManager.getConfigList().size());
    }

    @Test
    public void insertConfigNginxAccessLog() {
        String configId = "nginxAccessLogSample";
        MetricConfig config =
                new NginxAccessLogMetricConfig(configId,
                        "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"");
        insertConfig(config);

        System.out.println(metricConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(metricConfigManager.getConfigList()));
        assertNotNull(metricConfigManager.getConfig(configId));
        assertEquals(1, metricConfigManager.getConfigList().size());
    }

    public void insertConfig(MetricConfig config) {
        System.out.println(JMJson.toJsonString(config));
        metricConfigManager.insertConfig(config);
    }

    @Test
    public void testRemoveDataId() {
        insertConfig(new KeyValueDelimiterMetricConfig("keyValueDelimiterSample",
                null, "=", ":",
                "[{}\", ]").withBindDataIds("data1", "data2"));
        insertConfig(new NginxAccessLogMetricConfig("nginxAccessLogSample",
                "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"")
                .withBindDataIds("data1", "data2", "data3"));
        List<String> dataIdList =
                metricConfigManager.getConfigList().stream().map
                        (MetricConfig::getBindDataIds).flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(5, dataIdList.size());
        metricConfigManager.removeDataId("data2");
        dataIdList =
                metricConfigManager.getConfigList().stream().map
                        (MetricConfig::getBindDataIds).flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(3, dataIdList.size());
        metricConfigManager.removeDataId("data3");
        dataIdList =
                metricConfigManager.getConfigList().stream().map
                        (MetricConfig::getBindDataIds).flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(2, dataIdList.size());
        assertTrue(dataIdList.contains("data1"));
        assertFalse(dataIdList.contains("data2"));
        assertFalse(dataIdList.contains("data3"));
    }
}


