package kr.jm.metric.input.publisher;

import kr.jm.metric.config.input.ChunkType;
import kr.jm.metric.config.input.InputConfigManager;
import kr.jm.metric.config.input.StdInLineInputConfig;
import kr.jm.metric.data.Transfer;
import kr.jm.utils.helper.JMConsumer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InputPublisherTest {
    private InputPublisher inputPublisher;
    private InputConfigManager inputConfigManager;

    @Before
    public void setUp() {
        String configFilePathOrClasspath = "config/Input.json";
        this.inputConfigManager =
                new InputConfigManager(configFilePathOrClasspath);
    }

    @After
    public void tearDown() {
        this.inputPublisher.close();
    }

    @Test
    public void testStart() {
        String testString =
                "[{\"values\":[427,430],\"dstypes\":[\"derive\",\"derive\"]," +
                        "\"dsnames\":[\"rx\",\"tx\"],\"time\":1535336764.687," +
                        "\"interval\":5.000,\"host\":\"jm-macbook-pro-6.local\",\"plugin\":\"interface\",\"plugin_instance\":\"en5\",\"type\":\"if_packets\",\"type_instance\":\"\"}]";
        List<Transfer<String>> result = new ArrayList<>();
        this.inputPublisher = InputPublisherBuilder.build(this
                .inputConfigManager.getConfig("StdInJsonList"))
                .consumeWith(result::addAll).consumeWith(JMConsumer.getSOPL());
        this.inputPublisher.testInput(testString);
        Assert.assertEquals(
                "{\"values\":[427,430],\"dstypes\":[\"derive\",\"derive\"]," +
                        "\"dsnames\":[\"rx\",\"tx\"],\"time\":1.535336764687E9," +
                        "\"interval\":5.0,\"host\":\"jm-macbook-pro-6.local\"," +
                        "\"plugin\":\"interface\",\"plugin_instance\":\"en5\"," +
                        "\"type\":\"if_packets\",\"type_instance\":\"\"}",
                result.get(0).getData());

        testString =
                "{\"values\":[427,430],\"dstypes\":[\"derive\",\"derive\"]," +
                        "\"dsnames\":[\"rx\",\"tx\"],\"time\":1535336764.687," +
                        "\"interval\":5.000," +
                        "\"host\":\"jm-macbook-pro-6.local\"," +
                        "\"plugin\":\"interface\"," +
                        "\"plugin_instance\":\"en5\",\"type\":\"if_packets\"," +
                        "\"type_instance\":\"\"}\n{\"values\":[427,430],\"dstypes\":[\"derive\",\"derive\"],\"dsnames\":[\"rx\",\"tx\"],\"time\":1.535336764687E9,\"interval\":5.0,\"host\":\"jm-macbook-pro-6.local\",\"plugin\":\"interface\",\"plugin_instance\":\"en5\",\"type\":\"if_packets\",\"type_instance\":\"\"}\n";
        result = new ArrayList<>();
        this.inputPublisher = InputPublisherBuilder.build(new
                StdInLineInputConfig("MultilineTest", ChunkType.LINES))
                .consumeWith(result::addAll).consumeWith(JMConsumer.getSOPL());
        this.inputPublisher.testInput(testString);
        Assert.assertEquals(2, result.size());

    }
}