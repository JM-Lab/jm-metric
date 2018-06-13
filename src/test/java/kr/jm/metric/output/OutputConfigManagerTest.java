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
        this.outputConfigManager = new OutputConfigManager("OutputConfig.json");
    }

    @Test
    public void testGetOutput() {
        Assert.assertTrue(this.outputConfigManager.getOutputAsOpt("ESClient")
                .isPresent());
        System.out.println(
                this.outputConfigManager.getConfigMap().get("ESClient"));
        OutputConfigInterface outputConfig =
                this.outputConfigManager.getConfigAsOpt("StdOut").get();
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