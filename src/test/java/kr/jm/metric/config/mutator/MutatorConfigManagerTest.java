package kr.jm.metric.config.mutator;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.helper.JMJson;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MutatorConfigManagerTest {

    private MutatorConfigManager mutatorConfigManager;

    @Before
    public void setUp() {
        this.mutatorConfigManager =
                new MutatorConfigManager("config/Mutator.json");
    }

    @Test
    public void insertConfigDelimiter() {
        String mutatorId = "delimiterSample2";
        AbstractMutatorConfig config = null;
        try {
            config = new DelimiterMutatorConfig(mutatorId,
                    JMArrays.buildArray("field1", "field1"));
        } catch (Exception e) {
            assertEquals(e.getMessage(),
                    "Occur Duplicate field !!! - [field1, field1]");
        }
        config =
                new DelimiterMutatorConfig(mutatorId,
                        JMArrays.buildArray("field1", "field2"));
        insertConfig(config);

        System.out.println(mutatorConfigManager.getConfig(mutatorId));
        System.out.println(
                JMJson.toJsonString(mutatorConfigManager.getConfigMap()));
        assertNotNull(mutatorConfigManager.getConfig(mutatorId));
        assertEquals(10, mutatorConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigKeyValueDelimiter() {
        String mutatorId = "keyValueDelimiterSample2";
        AbstractMutatorConfig config =
                new KeyValueDelimiterMutatorConfig(mutatorId, "=", ":",
                        "[{}\", ]", null
                );
        insertConfig(config);

        System.out.println(mutatorConfigManager.getConfig(mutatorId));
        System.out.println(
                JMJson.toJsonString(mutatorConfigManager.getConfigMap()));
        assertNotNull(mutatorConfigManager.getConfig(mutatorId));
        assertEquals(10, mutatorConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigFormatted() {
        String mutatorId = "formattedSample2";
        AbstractMutatorConfig config =
                new FormattedMutatorConfig(mutatorId, true, "$ip - - $url",
                        Map.of("$ip", "ip", "$url", "url"));
        insertConfig(config);

        System.out.println(mutatorConfigManager.getConfig(mutatorId));
        System.out.println(
                JMJson.toJsonString(mutatorConfigManager.getConfigMap()));
        assertNotNull(mutatorConfigManager.getConfig(mutatorId));
        assertEquals(10, mutatorConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigApacheAccessLog() {
        String mutatorId = "apacheAccessLogSample2";
        String apacheCommonLogFormat =
                "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"";
        AbstractMutatorConfig config =
                new ApacheAccessLogMutatorConfig(mutatorId,
                        apacheCommonLogFormat);
        insertConfig(config);

        System.out.println(mutatorConfigManager.getConfig(mutatorId));
        System.out.println(
                JMJson.toJsonString(mutatorConfigManager.getConfigMap()));
        assertNotNull(mutatorConfigManager.getConfig(mutatorId));
        assertEquals(10, mutatorConfigManager.getConfigMap().size());
    }

    @Test
    public void insertConfigNginxAccessLog() {
        String mutatorId = "nginxAccessLogSample2";
        AbstractMutatorConfig config =
                new NginxAccessLogMutatorConfig(mutatorId,
                        "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"");
        insertConfig(config);

        System.out.println(mutatorConfigManager.getConfig(mutatorId));
        System.out.println(
                JMJson.toJsonString(mutatorConfigManager.getConfigMap()));
        assertNotNull(mutatorConfigManager.getConfig(mutatorId));
        assertEquals(10, mutatorConfigManager.getConfigMap().size());
    }

    private void insertConfig(AbstractMutatorConfig config) {
        System.out.println(JMJson.toJsonString(config));
        mutatorConfigManager.insertConfig(config);
    }

    @Test
    public void testLoadConfig() {
        assertEquals(14,
                mutatorConfigManager.loadConfig("testMutator.json")
                        .getConfigMap().size());
        assertEquals("keyValueDelimiterSample2",
                mutatorConfigManager.getConfig
                        ("keyValueDelimiterSample2").getMutatorId());
    }
}


