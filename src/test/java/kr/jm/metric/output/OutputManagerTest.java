package kr.jm.metric.output;

import kr.jm.metric.config.output.OutputConfigInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class OutputManagerTest {

    private OutputManager outputManager;

    @Before
    public void setUp() {
        this.outputManager = new OutputManager("outputConfig.json");
    }

    @Test
    public void testGetOutput() {
        OutputInterface esClient = this.outputManager.getOutput("ESClient");
        System.out.println(esClient.getOutputConfig());
        OutputConfigInterface outputConfig =
                this.outputManager.getOutput("StdOut").getOutputConfig();
        System.out.println(outputConfig.getOutputConfigType());
        Map<String, Object> fileConfigMap = outputConfig.extractConfigMap();
        System.out.println(fileConfigMap);
        Assert.assertEquals("true",
                fileConfigMap.get("enableJsonString").toString());
        Assert.assertEquals("STDOUT", fileConfigMap.get("outputConfigType"));
        System.out.println(outputManager.getOutputConfigMap());
        Assert.assertEquals(3, outputManager.getOutputConfigMap().size());

    }
}