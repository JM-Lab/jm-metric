package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.*;
import kr.jm.metric.config.mutator.field.DataType;
import kr.jm.metric.config.mutator.field.FieldConfigBuilder;
import kr.jm.metric.config.mutator.field.FormulaFieldConfig;
import kr.jm.metric.mutator.MutatorInterface;
import kr.jm.utils.helper.JMJson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MutatorTest {

    private MutatorConfigManager mutatorConfigManager;

    @Before
    public void setUp() {
        String configFilePathOrClasspath = "config/Mutator.json";
        this.mutatorConfigManager =
                new MutatorConfigManager(configFilePathOrClasspath);
    }

    @Test
    public void testJson() {
        String testString =
                "{\"values\":[427,430],\"dstypes\":[\"derive\",\"derive\"],\"dsnames\":[\"rx\",\"tx\"],\"time\":1535336764.687,\"interval\":5.000,\"host\":\"jm-macbook-pro-6.local\",\"plugin\":\"interface\",\"plugin_instance\":\"en5\",\"type\":\"if_packets\",\"type_instance\":\"\"}";
        MutatorConfigInterface mutatorConfig =
                mutatorConfigManager.getConfig("Json");
        MutatorInterface mutator = mutatorConfig.buildMutator();
        System.out.println(mutator);
        Map<String, Object> resultMap = mutator.mutate(testString);
        String result = JMJson.toJsonString(resultMap);
        System.out.println(result);
        System.out.println(JMJson.toJsonString(mutatorConfig));
        Assert.assertEquals(
                "{\"values\":[427,430],\"dstypes\":[\"derive\",\"derive\"],\"dsnames\":[\"rx\",\"tx\"],\"time\":1.535336764687E9,\"interval\":5.0,\"host\":\"jm-macbook-pro-6.local\",\"plugin\":\"interface\",\"plugin_instance\":\"en5\",\"type\":\"if_packets\",\"type_instance\":\"\"}",
                result);
    }

    @Test
    public void testDelimiter() {
        String testString =
                "\"강남구\",\"개포1동\",\"2\",\"02-577-0369\",\"서울 강남구 개포로 311\",\"25406.1\",\"25406.1\",\"232\",\"2\",\"2017\",\"1\",\"34506037\",\"40393930\",\"3675900\",\"1358.17\",\"1589.93\",\"144.68\",\"78575867\",\"3092.79\",\"511053\"";
        MutatorConfigInterface mutatorConfig = new DelimiterMutatorConfig
                ("delimiterTest", ",", "\"",
                        new FieldConfigBuilder().setRawData(true)
                                .setIgnore(List.of("전화번호", "단지 명"))
                                .setCombinedFields(null).setFormulaFields(
                                new FormulaFieldConfig[]{new FormulaFieldConfig(
                                        new String[]{"총 관리비 합계", "세대 수"},
                                        "세대별 평균 관리비",
                                        null, "총 관리비 합계 / 세대 수", null)})
                                .setDataType(null).setDateFormat(null)
                                .setFilter(null).createFieldConfig(), "자치구 명",
                        "동 명", "단지 명", "전화번호", "단지 주소",
                        "연 면적", "관리비 부과 면적", "세대 수", "동 수", "년", "월",
                        "총 공용 관리 비용", "총 세대 사용 비용", "총 장기 구선 충당금",
                        "미터제곱 당 공용 관리 비용", "미터제곱 당 세대 사용 비용",
                        "미터제곱 당 장기 수선 충당금", "총 관리비 합계", "미터 제곱당 관리비 합계", "잡수입");
        MutatorInterface mutator = mutatorConfig.buildMutator();
        System.out.println(mutator);
        Map<String, Object> resultMap = mutator.mutate(testString);
        Assert.assertNull(resultMap.get("전화번호"));
        Assert.assertNull(resultMap.get("단지 명"));
        Assert.assertEquals(338689.0818965517, resultMap.get("세대별 평균 관리비"));

        String result = JMJson.toJsonString(resultMap);
        System.out.println(result);
        System.out.println(JMJson.toJsonString(mutatorConfig));
        Assert.assertEquals(
                "{\"총 관리비 합계\":\"78575867\",\"자치구 명\":\"강남구\",\"년\":\"2017\",\"총 세대 사용 비용\":\"40393930\",\"미터 제곱당 관리비 합계\":\"3092.79\",\"미터제곱 당 공용 관리 비용\":\"1358.17\",\"잡수입\":\"511053\",\"총 장기 구선 충당금\":\"3675900\",\"연 면적\":\"25406.1\",\"관리비 부과 면적\":\"25406.1\",\"동 수\":\"2\",\"미터제곱 당 세대 사용 비용\":\"1589.93\",\"단지 주소\":\"서울 강남구 개포로 311\",\"월\":\"1\",\"세대별 평균 관리비\":338689.0818965517,\"세대 수\":\"232\",\"@rawData\":\"\\\"강남구\\\",\\\"개포1동\\\",\\\"2\\\",\\\"02-577-0369\\\",\\\"서울 강남구 개포로 311\\\",\\\"25406.1\\\",\\\"25406.1\\\",\\\"232\\\",\\\"2\\\",\\\"2017\\\",\\\"1\\\",\\\"34506037\\\",\\\"40393930\\\",\\\"3675900\\\",\\\"1358.17\\\",\\\"1589.93\\\",\\\"144.68\\\",\\\"78575867\\\",\\\"3092.79\\\",\\\"511053\\\"\",\"총 공용 관리 비용\":\"34506037\",\"동 명\":\"개포1동\",\"미터제곱 당 장기 수선 충당금\":\"144.68\"}",
                result);
    }

    @Test
    public void testKeyValueDelimiter() {
        String testString =
                "{dsnames=rx, plugin_instance=en5, type_instance=, plugin=interface, values=427, host=jm-macbook-pro-6.local, interval=5.0, time=1.535336764687E9, type=if_packets, dstypes=derive}";
        MutatorConfigInterface mutatorConfig =
                new KeyValueDelimiterMutatorConfig("KeyValueDelimiterTest",
                        "=", ", ", "[{}]");
        MutatorInterface mutator = mutatorConfig.buildMutator();
        Map<String, Object> resultMap = mutator.mutate(testString);
        System.out.println(resultMap);
        String result = JMJson.toJsonString(resultMap);
        System.out.println(result);
        System.out.println(JMJson.toJsonString(mutatorConfig));
        Assert.assertEquals(
                "{\"dsnames\":\"rx\",\"plugin_instance\":\"en5\",\"type_instance\":\"\",\"plugin\":\"interface\",\"values\":\"427\",\"host\":\"jm-macbook-pro-6.local\",\"interval\":\"5.0\",\"time\":\"1.535336764687E9\",\"type\":\"if_packets\",\"dstypes\":\"derive\"}",
                result);
    }

    @Test
    public void testFormatted() {
        String testString =
                "myhost_example_com.cpu-2.cpu-idle 98.6103 1329168255";
        MutatorConfigInterface mutatorConfig = new FormattedMutatorConfig
                ("formattedMutatorTest", true, "%hostname" +
                        "\\.%plugin-%pluginInstance\\.\\S+-%typeInstance %value %timestamp",
                        Map.of("%hostname", "hostname", "%plugin", "plugin",
                                "%pluginInstance", "pluginInstance",
                                "%typeInstance", "typeInstance", "%value",
                                "value", "%timestamp", "timestamp"),
                        new FieldConfigBuilder()
                                .setDataType(Map.of("value", DataType.NUMBER,
                                        "timestamp", DataType.LONG))
                                .createFieldConfig());

        MutatorInterface mutator = mutatorConfig.buildMutator();
        System.out.println(mutator);
        Map<String, Object> resultMap = mutator.mutate(testString);
        String result = JMJson.toJsonString(resultMap);
        System.out.println(result);
        System.out.println(JMJson.toJsonString(mutatorConfig));
        Assert.assertEquals(
                "{\"hostname\":\"myhost_example_com\",\"pluginInstance\":\"2\",\"plugin\":\"cpu\",\"typeInstance\":\"idle\",\"value\":98.6103,\"timestamp\":1329168255}",
                result);
    }
}