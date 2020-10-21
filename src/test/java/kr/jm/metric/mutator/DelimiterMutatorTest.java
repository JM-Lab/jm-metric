package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.DelimiterMutatorConfig;
import kr.jm.utils.JMArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class DelimiterMutatorTest {

    private DelimiterMutator delimiterMutator;

    @Before
    public void setUp() {
    }

    @Test
    public void testBuildFieldStringMap() {

        DelimiterMutatorConfig inputConfig =
                new DelimiterMutatorConfig("delimiterTest",
                        JMArrays.buildArray("field1", "field2", "field3"));
        System.out.println(Arrays.toString(inputConfig.getFields()));
        String testString = "172.22.206.86 - - [08/Jun/2015:16:59:59 +0900] " +
                "\"POST /app/5104 HTTP/1.1\" 200 448 \"-\" \"Jakarta Commons-HttpClient/3.1\" 184977";

        this.delimiterMutator = new DelimiterMutator(inputConfig);
        Assert.assertEquals(3, delimiterMutator.mutate(testString).size());
        Assert.assertEquals("172.22.206.86",
                delimiterMutator.mutate(testString).get("field1"));

        inputConfig = new DelimiterMutatorConfig("delimiterTest1");
        this.delimiterMutator = new DelimiterMutator(inputConfig);
        System.out.println(Arrays.toString(inputConfig.getFields()));
        Map<String, Object> fieldObjectMap1 =
                delimiterMutator.mutate(testString);
        System.out.println(fieldObjectMap1);
        System.out.println(Arrays.toString(inputConfig.getFields()));

        inputConfig = new DelimiterMutatorConfig("delimiterTest2",
                " \\[|\\] \"|\" \"| \"|\" | ");
        this.delimiterMutator = new DelimiterMutator(inputConfig);
        System.out.println(Arrays.toString(inputConfig.getFields()));
        Map<String, Object> fieldObjectMap2 =
                delimiterMutator.mutate(testString);
        System.out.println(fieldObjectMap2);
        System.out.println(Arrays.toString(inputConfig.getFields()));
        Assert.assertEquals(fieldObjectMap1.size(), fieldObjectMap2.size());

        inputConfig = new DelimiterMutatorConfig("delimiterTest3",
                " ", "[\\\"\\[\\]]");
        this.delimiterMutator = new DelimiterMutator(inputConfig);
        System.out.println(Arrays.toString(inputConfig.getFields()));
        Assert.assertEquals(0, inputConfig.getFields().length);
        Map<String, Object> fieldObjectMap3 =
                delimiterMutator.mutate(testString);
        System.out.println(fieldObjectMap3);
        System.out.println(Arrays.toString(inputConfig.getFields()));
        Assert.assertEquals(fieldObjectMap2.size(), fieldObjectMap3.size());
        Assert.assertEquals(fieldObjectMap2.toString(),
                fieldObjectMap3.toString());


    }
}