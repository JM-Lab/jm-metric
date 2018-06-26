package kr.jm.metric.config.mutating;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMJson;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        System.out.println(mutatingConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigKeyValueDelimiter() {
        String configId = "keyValueDelimiterSample2";
        MutatingConfig config =
                new KeyValueDelimiterMutatingConfig(configId, null, "=", ":",
                        "[{}\", ]");
        insertConfig(config);

        System.out.println(mutatingConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getConfig(configId));
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

        System.out.println(mutatingConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getConfig(configId));
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

        System.out.println(mutatingConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigNginxAccessLog() {
        String configId = "nginxAccessLogSample2";
        MutatingConfig config =
                new NginxAccessLogMutatingConfig(configId,
                        "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"");
        insertConfig(config);

        System.out.println(mutatingConfigManager.getConfig(configId));
        System.out.println(
                JMJson.toJsonString(mutatingConfigManager.getConfigMap()));
        assertNotNull(mutatingConfigManager.getConfig(configId));
        assertEquals(9, mutatingConfigManager.getConfigMap().size());
    }

    private void insertConfig(MutatingConfig config) {
        System.out.println(JMJson.toJsonString(config));
        mutatingConfigManager.insertConfig(config);
    }

    @Test
    public void testLoadConfig() {
        assertEquals(9,
                mutatingConfigManager.loadConfig("testMutatingConfig.json")
                        .getConfigMap().size());
        assertEquals("keyValueDelimiterSample2",
                mutatingConfigManager.getConfig
                        ("keyValueDelimiterSample2").getConfigId());
    }
}


