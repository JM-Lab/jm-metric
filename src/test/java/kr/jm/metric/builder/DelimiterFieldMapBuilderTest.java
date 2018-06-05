package kr.jm.metric.builder;

import kr.jm.metric.config.mutating.DelimiterMutatingConfig;
import kr.jm.utils.datastructure.JMArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class DelimiterFieldMapBuilderTest {

    private DelimiterFieldMapBuilder delimiterParser;

    @Before
    public void setUp() {
        this.delimiterParser = new DelimiterFieldMapBuilder();
    }

    @Test
    public void testBuildFieldStringMap() {

        DelimiterMutatingConfig inputConfig =
                new DelimiterMutatingConfig("delimiterTest",
                        JMArrays.buildArray("field1", "field2", "field3"));
        System.out.println(inputConfig.getFields());
        String testString = "172.22.206.86 - - [08/Jun/2015:16:59:59 +0900] " +
                "\"POST /app/5104 HTTP/1.1\" 200 448 \"-\" \"Jakarta Commons-HttpClient/3.1\" 184977";
        Assert.assertEquals(3, delimiterParser
                .buildFieldObjectMap(inputConfig, testString).size());
        Assert.assertEquals("172.22.206.86", delimiterParser
                .buildFieldObjectMap(inputConfig, testString).get("field1"));

        inputConfig = new DelimiterMutatingConfig("delimiterTest1");
        System.out.println(inputConfig.getFields());
        Map<String, Object> fieldObjectMap1 = delimiterParser
                .buildFieldObjectMap(inputConfig, testString);
        System.out.println(fieldObjectMap1);
        System.out.println(inputConfig.getFields());

        inputConfig = new DelimiterMutatingConfig("delimiterTest2",
                " \\[|\\] \"|\" \"| \"|\" | ");
        System.out.println(inputConfig.getFields());
        Map<String, Object> fieldObjectMap2 = delimiterParser
                .buildFieldObjectMap(inputConfig, testString);
        System.out.println(fieldObjectMap2);
        System.out.println(inputConfig.getFields());
        Assert.assertEquals(fieldObjectMap1.size(), fieldObjectMap2.size());

        inputConfig = new DelimiterMutatingConfig("delimiterTest3",
                " ", "[\\\"\\[\\]]");
        System.out.println(inputConfig.getFields());
        Assert.assertEquals(0, inputConfig.getFields().length);
        Map<String, Object> fieldObjectMap3 = delimiterParser
                .buildFieldObjectMap(inputConfig, testString);
        System.out.println(fieldObjectMap3);
        System.out.println(inputConfig.getFields());
        Assert.assertEquals(fieldObjectMap2.size(), fieldObjectMap3.size());
        Assert.assertEquals(fieldObjectMap2.toString(),
                fieldObjectMap3.toString());


    }
}