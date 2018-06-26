package kr.jm.metric.config.output;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class OutputConfigManagerTest {

    private OutputConfigManager outputConfigManager;

    @Before
    public void setUp() {
        this.outputConfigManager = new OutputConfigManager("OutputConfig.json");
    }

    @Test
    public void testGetOutput() {
        Assert.assertTrue(
                this.outputConfigManager.getMutatingConfigAsOpt("ESClient")
                .isPresent());
        System.out.println(
                this.outputConfigManager.getConfigMap().get("ESClient"));
        OutputConfigInterface outputConfig =
                this.outputConfigManager.getMutatingConfigAsOpt("StdOut").get();
        System.out.println(outputConfig.getOutputConfigType());
        Map<String, Object> fileConfigMap = outputConfig.extractConfigMap();
        System.out.println(fileConfigMap);
        Assert.assertEquals("true",
                fileConfigMap.get("enableJsonString").toString());
        Assert.assertEquals("STDOUT", fileConfigMap.get("outputConfigType"));
        System.out.println(outputConfigManager.getConfigMap());
        Assert.assertEquals(4, outputConfigManager.getConfigMap().size());

    }
}