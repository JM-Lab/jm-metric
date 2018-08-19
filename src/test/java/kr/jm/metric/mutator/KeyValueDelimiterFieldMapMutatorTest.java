package kr.jm.metric.mutator;

import kr.jm.metric.config.mutator.KeyValueDelimiterMutatorConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class KeyValueDelimiterFieldMapMutatorTest {

    private KeyValueDelimiterFieldMapMutator keyValueDelimiterParser;

    @Test
    public void buildFieldStringMap() {
        String targetString =
                "{remoteUser=frank, request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, remoteHost=127.0.0.1, sizeByte=2326, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), remoteLogName=-, timestamp=10/Oct/2000:13:55:36 -0700, httpStatusCode=200}";
        Assert.assertEquals(Collections.emptyMap(),
                new KeyValueDelimiterFieldMapMutator().mutate(targetString));
        this.keyValueDelimiterParser = new KeyValueDelimiterFieldMapMutator(
                new KeyValueDelimiterMutatorConfig("keyValueDelimiterTest",
                        null, "=", ", ", "[{}]"));
        Map<String, Object> fieldObjectMap =
                this.keyValueDelimiterParser.mutate(targetString);
        System.out.println(fieldObjectMap);
        Assert.assertEquals(9, fieldObjectMap.size());
        Assert.assertEquals("GET /apache_pb.gif HTTP/1.0",
                fieldObjectMap.get("request"));
    }
}