package kr.jm.metric.mutating.builder;

import kr.jm.metric.config.mutating.KeyValueDelimiterMutatingConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class KeyValueDelimiterFieldMapBuilderTest {

    private KeyValueDelimiterFieldMapBuilder keyValueDelimiterParser;

    @Before
    public void setUp() {
        this.keyValueDelimiterParser = new KeyValueDelimiterFieldMapBuilder();
    }

    @Test
    public void buildFieldStringMap() {
        KeyValueDelimiterMutatingConfig
                inputConfig =
                new KeyValueDelimiterMutatingConfig("keyValueDelimiterTest");
        String targetString =
                "{remoteUser=frank, request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, remoteHost=127.0.0.1, sizeByte=2326, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), remoteLogName=-, timestamp=10/Oct/2000:13:55:36 -0700, httpStatusCode=200}";
        Assert.assertEquals(Collections.emptyMap(),
                this.keyValueDelimiterParser
                        .buildFieldObjectMap(inputConfig, targetString));
        inputConfig =
                new KeyValueDelimiterMutatingConfig("keyValueDelimiterTest",
                        null, "=", ", ", "[{}]");
        Map<String, Object> fieldObjectMap = this.keyValueDelimiterParser
                .buildFieldObjectMap(inputConfig, targetString);
        System.out.println(fieldObjectMap);
        Assert.assertEquals(9, fieldObjectMap.size());
        Assert.assertEquals("GET /apache_pb.gif HTTP/1.0",
                fieldObjectMap.get("request"));
    }
}