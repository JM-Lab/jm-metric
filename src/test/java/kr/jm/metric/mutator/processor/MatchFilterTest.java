package kr.jm.metric.mutator.processor;

import kr.jm.metric.config.mutator.field.FilterConfig;
import kr.jm.utils.helper.JMJson;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.regex.Pattern;

public class MatchFilterTest {

    @Test
    public void testFilter() {
        System.out.println(Pattern.compile("\\d\\.\\d").matcher("GET " +
                "/apache_pb.gif HTTP/1.0").find());
        System.out.println(Pattern.compile("200").matcher("200").find());
        System.out.println(Pattern.compile("200").matcher("200").matches());
        System.out.println(Pattern.compile("200").matcher("20").lookingAt());
        MatchFilter matchFilter = new MatchFilter("testMatchFilter",
                Map.of("request", new FilterConfig("\\d\\.\\d", false),
                        "statusCode", new FilterConfig("\\", false),
                        "userAgent",
                        new FilterConfig("Firefox", true)));
        System.out.println(matchFilter);
        Assert.assertEquals("MatchFilter(mutateId=testMatchFilter, " +
                        "filterPatternMap={request=\\d\\.\\d, userAgent=Firefox}, " +
                        "negateConfigMap={request=false, userAgent=true})",
                matchFilter.toString());

        Map<String, Object> testMap = JMJson.toMap(
                "{\"remoteUser\":\"frank\",\"request\":\"GET /apache_pb.gif HTTP/1.0\",\"referer\":\"http://www.example.com/start.html\",\"receivedTimestamp\":\"10/Oct/2000:13:55:36 -0700\",\"remoteHost\":\"127.0.0.1\",\"sizeByte\":\"2326\",\"userAgent\":\"Mozilla/4.08 [en] (Win98; I ;Nav)\",\"remoteLogName\":\"-\",\"statusCode\":\"200\"}");

        Assert.assertTrue(matchFilter.filter(testMap));

    }
}