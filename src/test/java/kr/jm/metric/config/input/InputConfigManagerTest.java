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
        this.inputConfigManager = new InputConfigManager("config/Input.json");
    }

    @Test
    public void testGetInput() {
        System.out.println(
                JMJson.toJsonString(inputConfigManager.getConfigMap()));
        InputInterface stdInput =
                this.inputConfigManager.getConfig("Stdin").buildInput();
        Assert.assertEquals("Stdin", stdInput.getInputId());
        Assert.assertEquals(2, inputConfigManager.getConfigMap().size());
    }
}