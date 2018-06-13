package kr.jm.metric.config;

import kr.jm.metric.config.mutating.*;
import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMJson;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MutatingConfigManagerTest {

    private MutatingConfigManager mutatingConfigManager;

    @Before
    public void setUp() {
        this.mutatingConfigManager =
                new MutatingConfigManager("MutatingConfig.json");
    }

    @Test
    public void insertConfigDelimiter() {
        String configId = "delimiterSample2";
        MutatingConfig config = null;
        try {
            config = new DelimiterMutatingConfig(configId,
                    JMArrays.buildArray("field1", "field1"));
        } catch (Exception e) {
            assertEquals(e.getMessage(),
                    "Occur Duplicate field !!! - [field1, field1]");
        }
        config =
                new DelimiterMutatingConfig(configId,
                        JMArrays.buildArray("field1", "field2"));
        insertConfig(config);

        System.out.println(mutatingConfigManager.getMutatingConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getMutatingConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigKeyValueDelimiter() {
        String configId = "keyValueDelimiterSample2";
        MutatingConfig config =
                new KeyValueDelimiterMutatingConfig(configId, null, "=", ":",
                        "[{}\", ]");
        insertConfig(config);

        System.out.println(mutatingConfigManager.getMutatingConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getMutatingConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigFormatted() {
        String configId = "formattedSample2";
        MutatingConfig config =
                new FormattedMutatingConfig(configId, "$ip - - $url",
                        Map.of("$ip", "ip", "$url",
                                "url"));
        insertConfig(config);

        System.out.println(mutatingConfigManager.getMutatingConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getMutatingConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigApacheAccessLog() {
        String configId = "apacheAccessLogSample2";
        String apacheCommonLogFormat =
                "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"";
        MutatingConfig config =
                new ApacheAccessLogMutatingConfig(configId,
                        apacheCommonLogFormat);
        insertConfig(config);

        System.out.println(mutatingConfigManager.getMutatingConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getMutatingConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigNginxAccessLog() {
        String configId = "nginxAccessLogSample2";
        MutatingConfig config =
                new NginxAccessLogMutatingConfig(configId,
                        "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"");
        insertConfig(config);

        System.out.println(mutatingConfigManager.getMutatingConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getMutatingConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    public void insertConfig(MutatingConfig config) {
        System.out.println(JMJson.toJsonString(config));
        mutatingConfigManager.insertConfig(config);
    }

    @Test
    public void testRemoveDataId() {
        insertConfig(
                new KeyValueDelimiterMutatingConfig("keyValueDelimiterSample",
                        null, "=", ":",
                        "[{}\", ]").withBindDataIds("data1", "data2"));
        insertConfig(new NginxAccessLogMutatingConfig("nginxAccessLogSample",
                "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"")
                .withBindDataIds("data1", "data2", "data3"));
        List<String> dataIdList =
                mutatingConfigManager.getConfigMap().values().stream()
                        .map(MutatingConfig::getBindDataIds)
                        .flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(5, dataIdList.size());
        mutatingConfigManager.removeDataId("data2");
        dataIdList =
                mutatingConfigManager.getConfigMap().values().stream().map
                        (MutatingConfig::getBindDataIds).flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(3, dataIdList.size());
        mutatingConfigManager.removeDataId("data3");
        dataIdList =
                mutatingConfigManager.getConfigMap().values().stream().map
                        (MutatingConfig::getBindDataIds).flatMap(Set::stream)
                        .collect(Collectors.toList());
        System.out.println(dataIdList);
        assertEquals(2, dataIdList.size());
        assertTrue(dataIdList.contains("data1"));
        assertFalse(dataIdList.contains("data2"));
        assertFalse(dataIdList.contains("data3"));
    }

    @Test
    public void testLoadConfig() {
        assertEquals(9,
                mutatingConfigManager.loadConfig("testMutatingConfig.json")
                        .getConfigMap().size());
        assertEquals("keyValueDelimiterSample2",
                mutatingConfigManager.getMutatingConfig
                        ("keyValueDelimiterSample2").getConfigId());
    }
}


