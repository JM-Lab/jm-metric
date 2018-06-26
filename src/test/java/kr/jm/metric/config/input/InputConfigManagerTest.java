package kr.jm.metric.config.input;

import kr.jm.metric.input.InputInterface;
import kr.jm.utils.helper.JMJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputConfigManagerTest {

    private InputConfigManager inputConfigManager;

    @Before
    public void setUp() {
        this.inputConfigManager = new InputConfigManager("InputConfig.json");
    }

    @Test
    public void testGetInput() {
        System.out.println(
                JMJson.toJsonString(inputConfigManager.getConfigMap()));
        InputInterface stdInput =
                this.inputConfigManager.getConfig("StdIn").buildInput();
        Assert.assertEquals("StdIn", stdInput.getInputId());
        Assert.assertEquals(2, inputConfigManager.getConfigMap().size());
    }
}