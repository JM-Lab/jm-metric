package kr.jm.metric.config;

import kr.jm.metric.config.mutator.MutatorConfigInterface;
import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.utils.helper.JMResources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static kr.jm.metric.config.mutator.field.DateFormatType.EPOCH;

public class JMMetricConfigManagerTest {

    private JMMetricConfigManager jmMetricConfigManager;

    @Before
    public void setUp() {
        this.jmMetricConfigManager =
                new JMMetricConfigManager(JMResources
                        .getResourceURL("JMMetricConfigTest.json").getPath());
    }

    @Test
    public void testJMMetricConfigManager() {
        this.jmMetricConfigManager.printAllConfig();

        Assert.assertEquals("Stdin", this
                .jmMetricConfigManager.getInputConfigId());
        Assert.assertEquals("CombinedLogFormat", this
                .jmMetricConfigManager.getMutatorConfigId());
        Assert.assertEquals(2,
                this.jmMetricConfigManager.getOutputConfigIds().length);
        Assert.assertEquals("Stdout",
                Arrays.stream(this.jmMetricConfigManager.getOutputConfigIds())
                        .sorted().toArray()[0]);

        OutputConfigInterface outputConfig = this.jmMetricConfigManager
                .getOutputConfig("Stdout");
        System.out.println(outputConfig);
        Assert.assertEquals(false,
                outputConfig.extractConfigMap().get("enableJsonString"));

        MutatorConfigInterface mutatorConfig =
                this.jmMetricConfigManager.getMutatorConfig(
                        this.jmMetricConfigManager
                                .getMutatorConfigId());
        System.out.println(mutatorConfig);
        Assert.assertEquals(EPOCH, mutatorConfig.getFieldConfig()
                .getDateFormat().get("receivedTimestamp").getChangeDateConfig
                        ().getDateFormatType());
        Assert.assertTrue(mutatorConfig.getFieldConfig().isRawData());
        Assert.assertEquals(
                mutatorConfig.getFieldConfig().getDataType().get("sizeByte").name(),
                "NUMBER");


    }
}