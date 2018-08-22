package kr.jm.metric.config;

import kr.jm.metric.config.mutator.MutatorConfigInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static kr.jm.metric.config.mutator.field.DateFormatType.EPOCH;

public class JMMetricConfigManagerTest {

    private JMMetricConfigManager jmMetricConfigManager;

    @Before
    public void setUp() {
        this.jmMetricConfigManager =
                new JMMetricConfigManager("JMMetricConfigTest.json");
    }

    @Test
    public void testJMMetricConfigManager() {
        MutatorConfigInterface mutatorConfig =
                this.jmMetricConfigManager.getMutatorConfig();
        System.out.println(mutatorConfig);
        Assert.assertEquals(EPOCH, mutatorConfig.getFieldConfig()
                .getDateFormat().get("receivedTimestamp").getChangeDateConfig
                        ().getDateFormatType());
        Assert.assertFalse(mutatorConfig.getFieldConfig().isRawData());

    }
}