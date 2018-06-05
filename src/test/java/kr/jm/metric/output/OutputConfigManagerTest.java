package kr.jm.metric.output;

import kr.jm.metric.config.output.OutputConfigInterface;
import kr.jm.metric.config.output.OutputConfigManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class OutputConfigManagerTest {

    private OutputConfigManager outputConfigManager;

    @Before
    public void setUp() {
        this.outputConfigManager = new OutputConfigManager();
    }

    @Test
    public void testGetOutput() {
        OutputInterface esClient =
                this.outputConfigManager.getOutput("ESClient");
        System.out.println(esClient.getOutputConfig());
        OutputConfigInterface outputConfig =
                this.outputConfigManager.getOutput("StdOut").getOutputConfig();
        System.out.println(outputConfig.getOutputConfigType());
        Map<String, Object> fileConfigMap = outputConfig.extractConfigMap();
        System.out.println(fileConfigMap);
        Assert.assertEquals("true",
                fileConfigMap.get("enableJsonString").toString());
        Assert.assertEquals("STDOUT", fileConfigMap.get("outputConfigType"));
        System.out.println(outputConfigManager.getOutputConfigMap());
        Assert.assertEquals(4, outputConfigManager.getOutputConfigMap().size());

    }
}