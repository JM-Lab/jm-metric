package kr.jm.metric.builder;

import kr.jm.metric.config.NginxAccessLogMetricConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class NginxAccessLogFieldMapBuilderTest {

    private NginxAccessLogFieldMapBuilder parser;
    private String configId;

    @Before
    public void setUp() throws Exception {
        this.parser =
                new NginxAccessLogFieldMapBuilder();
        this.configId = "testNginxAccessLog";
    }

    @Test
    public void testParser() {
        String nginxLogFormat =
                "$remote_addr - $remote_user [$time_local] \"$request\" $status $body_bytes_sent \"$http_referer\" \"$http_user_agent\"";
        String groupRegex = parser.buildGroupRegexString(configId,
                nginxLogFormat, null);
        System.out.println(groupRegex);
        String targetString =
                "127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] \"GET /apache_pb.gif HTTP/1.0\" 200 2326 \"http://www.example.com/start.html\" \"Mozilla/4.08 [en] (Win98; I ;Nav)\"";
        Map<String, Object> fieldObjectMap = parser.buildFieldObjectMap(
                new NginxAccessLogMetricConfig(configId, nginxLogFormat),
                targetString);
        System.out.println(fieldObjectMap);
        Assert.assertEquals(
                "{remoteUser=frank, request=GET /apache_pb.gif HTTP/1.0, referer=http://www.example.com/start.html, remoteHost=127.0.0.1, sizeByte=2326, userAgent=Mozilla/4.08 [en] (Win98; I ;Nav), timestamp=10/Oct/2000:13:55:36 -0700, statusCode=200}",
                fieldObjectMap.toString());
    }


}